package com.touchgrass.service

import android.app.AppOpsManager
import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Typeface
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.touchgrass.MainActivity
import com.touchgrass.data.datastore.UserPreferencesDataStore
import com.touchgrass.utils.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

@AndroidEntryPoint
class AppBlockingService : Service() {

    @Inject lateinit var preferencesDataStore: UserPreferencesDataStore
    @Inject lateinit var notificationHelper: NotificationHelper

    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var monitorJob: Job? = null

    // Debounce: don't re-trigger blocking for the same app within 1.5s
    private var lastBlockedPackage: String? = null
    private var lastBlockTime: Long = 0

    // Overlay state — all mutations must happen on Main thread
    private var overlayRoot: FrameLayout? = null
    private var timerView: TextView? = null
    private var bypassBtn: Button? = null
    private var countdownJob: Job? = null
    private var overlayPackage: String? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startMonitoring()
            ACTION_STOP -> {
                stopMonitoring()
                stopSelf()
            }
        }
        return START_STICKY
    }

    private fun startMonitoring() {
        val notification = notificationHelper.buildFocusNotification("Active", "App Blocker")
        ServiceCompat.startForeground(
            this, NotificationHelper.NOTIF_BLOCKING_ID, notification,
            android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC,
        )
        monitorJob = serviceScope.launch {
            while (isActive) {
                checkForegroundApp()
                delay(1000)
            }
        }
    }

    private suspend fun checkForegroundApp() {
        if (!hasUsageStatsPermission()) return

        // Only block during active focus sessions
        if (!FocusSessionService.isFocusActive) {
            if (overlayRoot != null) removeOverlay()
            return
        }

        val prefs = preferencesDataStore.userPreferences.first()
        if (prefs.blockedApps.isEmpty()) return

        val foreground = getCurrentForegroundApp() ?: return

        if (foreground in prefs.blockedApps && foreground != packageName && !isBypassed(foreground)) {
            // Already showing overlay for this package — just keep it
            if (overlayPackage == foreground && overlayRoot != null) return

            val now = System.currentTimeMillis()
            if (foreground == lastBlockedPackage && (now - lastBlockTime) < 1_500) return
            lastBlockedPackage = foreground
            lastBlockTime = now
            showBlockingOverlay(foreground)
        } else if (overlayRoot != null) {
            // User left the blocked app — remove overlay
            removeOverlay()
        }
    }

    private fun showBlockingOverlay(blockedPkg: String) {
        val appName = getAppLabel(blockedPkg)
        if (Settings.canDrawOverlays(this)) {
            serviceScope.launch(Dispatchers.Main) { buildAndShowOverlay(blockedPkg, appName) }
        } else {
            // Fallback: fullscreen notification (works on older Android)
            showFallbackNotification(blockedPkg, appName)
        }
    }

    private fun buildAndShowOverlay(blockedPkg: String, appName: String) {
        // Remove any existing overlay first
        dismissOverlayInternal()

        overlayPackage = blockedPkg
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager

        // ── Root ──────────────────────────────────────────────────────────────
        val root = object : FrameLayout(this) {
            override fun dispatchKeyEvent(event: KeyEvent): Boolean {
                if (event.keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                    goHome()
                    return true
                }
                return super.dispatchKeyEvent(event)
            }
        }
        root.setBackgroundColor(0xF5070711.toInt())
        root.isFocusableInTouchMode = true

        // ── Column ────────────────────────────────────────────────────────────
        val col = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT,
            )
            setPadding(80, 0, 80, 0)
        }
        col.gravity = Gravity.CENTER

        col.addView(textView("🚫", 52f, Color.WHITE, bold = false))
        col.addView(spacer(20))
        col.addView(textView("BLOCKED", 13f, 0xFFEF4444.toInt(), bold = true).also {
            it.letterSpacing = 0.3f
        })
        col.addView(spacer(8))
        col.addView(textView(appName, 30f, Color.WHITE, bold = true))
        col.addView(spacer(8))
        col.addView(textView(
            "This app is on your focus block list.\nGo touch some grass instead.",
            14f, 0xFF9999BB.toInt(), bold = false,
        ).also { it.gravity = Gravity.CENTER })
        col.addView(spacer(48))

        // Timer ring placeholder — just a big countdown number
        val timer = textView("30", 64f, Color.WHITE, bold = true)
        timerView = timer
        col.addView(timer)

        col.addView(spacer(4))
        col.addView(textView("seconds before bypass", 12f, 0xFF666688.toInt(), bold = false))
        col.addView(spacer(48))

        // Take a Break button
        val homeBtn = button("Take a Break 🌿", 0xFF22C55E.toInt()) { goHome() }
        col.addView(homeBtn)
        col.addView(spacer(12))

        // Emergency bypass — hidden until countdown finishes
        val bypass = button("Emergency Bypass (-50 XP) ⚠️", Color.TRANSPARENT) {
            addBypass(blockedPkg)
            dismissOverlayInternal()
        }.also {
            it.setTextColor(0xFFEF4444.toInt())
            it.visibility = View.GONE
        }
        bypassBtn = bypass
        col.addView(bypass)

        root.addView(col)
        overlayRoot = root

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
            PixelFormat.OPAQUE,
        )

        try {
            wm.addView(root, params)
            root.requestFocus()
            startCountdown()
        } catch (e: Exception) {
            overlayRoot = null
            timerView = null
            bypassBtn = null
            overlayPackage = null
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = serviceScope.launch(Dispatchers.Main) {
            var remaining = 30
            while (remaining > 0 && overlayRoot != null) {
                timerView?.text = remaining.toString()
                delay(1000)
                remaining--
            }
            if (overlayRoot != null) {
                timerView?.text = "!"
                timerView?.setTextColor(0xFFEF4444.toInt())
                bypassBtn?.visibility = View.VISIBLE
            }
        }
    }

    private fun goHome() {
        dismissOverlayInternal()
        try {
            startActivity(Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        } catch (_: Exception) {}
    }

    private fun removeOverlay() {
        serviceScope.launch(Dispatchers.Main) { dismissOverlayInternal() }
    }

    private fun dismissOverlayInternal() {
        countdownJob?.cancel()
        countdownJob = null
        overlayRoot?.let { view ->
            try {
                (getSystemService(WINDOW_SERVICE) as WindowManager).removeView(view)
            } catch (_: Exception) {}
        }
        overlayRoot = null
        timerView = null
        bypassBtn = null
        overlayPackage = null
    }

    private fun showFallbackNotification(blockedPkg: String, appName: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_SHOW_BLOCKING, true)
            putExtra(EXTRA_BLOCKED_PACKAGE, blockedPkg)
        }
        val pi = android.app.PendingIntent.getActivity(
            this, blockedPkg.hashCode(), intent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE,
        )
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        val n = NotificationCompat.Builder(this, NotificationHelper.CHANNEL_BLOCKING)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("$appName is blocked 🚫")
            .setContentText("Tap to open Touch Grass.")
            .setFullScreenIntent(pi, true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(false)
            .build()
        nm.notify(NotificationHelper.NOTIF_BLOCKING_OVERLAY_ID, n)
        try { startActivity(intent) } catch (_: Exception) {}
    }

    // ── View helpers ─────────────────────────────────────────────────────────

    private fun textView(text: String, sizeSp: Float, color: Int, bold: Boolean) =
        TextView(this).apply {
            this.text = text
            textSize = sizeSp
            setTextColor(color)
            gravity = Gravity.CENTER
            if (bold) typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

    private fun button(label: String, bgColor: Int, onClick: () -> Unit) =
        Button(this).apply {
            text = label
            setBackgroundColor(bgColor)
            setTextColor(Color.WHITE)
            textSize = 15f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                140,
            )
            setOnClickListener { onClick() }
        }

    private fun spacer(heightPx: Int) = android.view.View(this).apply {
        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightPx)
    }

    // ── Foreground app detection ──────────────────────────────────────────────

    private fun getCurrentForegroundApp(): String? {
        val usm = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val end = System.currentTimeMillis()
        return try {
            val events = usm.queryEvents(end - 5_000, end)
            var pkg: String? = null
            val event = UsageEvents.Event()
            while (events?.hasNextEvent() == true) {
                events.getNextEvent(event)
                if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) pkg = event.packageName
            }
            pkg ?: usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, end - 86_400_000L, end)
                ?.filter { it.lastTimeUsed > end - 3_000 }
                ?.maxByOrNull { it.lastTimeUsed }
                ?.packageName
        } catch (_: Exception) { null }
    }

    private fun hasUsageStatsPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), packageName)
        } else {
            @Suppress("DEPRECATION")
            appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), packageName)
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun getAppLabel(pkg: String): String = runCatching {
        packageManager.getApplicationLabel(packageManager.getApplicationInfo(pkg, 0)).toString()
    }.getOrDefault(pkg.substringAfterLast('.').replaceFirstChar { it.uppercase() })

    private fun stopMonitoring() {
        monitorJob?.cancel()
        notificationHelper.cancelFocusNotification()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.launch(Dispatchers.Main) { dismissOverlayInternal() }
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "com.touchgrass.BLOCKING_START"
        const val ACTION_STOP = "com.touchgrass.BLOCKING_STOP"
        const val EXTRA_SHOW_BLOCKING = "show_blocking_screen"
        const val EXTRA_BLOCKED_PACKAGE = "blocked_package"
        private const val BYPASS_DURATION_MS = 5 * 60 * 1000L

        private val bypassedPackages = ConcurrentHashMap<String, Long>()

        fun addBypass(packageName: String) {
            bypassedPackages[packageName] = System.currentTimeMillis() + BYPASS_DURATION_MS
        }

        fun isBypassed(packageName: String): Boolean {
            val expiry = bypassedPackages[packageName] ?: return false
            if (System.currentTimeMillis() >= expiry) {
                bypassedPackages.remove(packageName)
                return false
            }
            return true
        }
    }
}
