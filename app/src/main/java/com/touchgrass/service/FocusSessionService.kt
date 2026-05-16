package com.touchgrass.service

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.ServiceCompat
import com.touchgrass.data.repository.FocusRepository
import com.touchgrass.data.repository.GamificationRepository
import com.touchgrass.domain.model.FocusSessionType
import com.touchgrass.utils.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@AndroidEntryPoint
class FocusSessionService : Service() {

    @Inject lateinit var focusRepository: FocusRepository
    @Inject lateinit var gamificationRepository: GamificationRepository
    @Inject lateinit var notificationHelper: NotificationHelper

    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val binder = LocalBinder()

    private val _remainingSeconds = MutableStateFlow(0)
    val remainingSeconds: StateFlow<Int> = _remainingSeconds.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private var currentSessionId: Long = -1
    private var timerJob: Job? = null
    private var currentSessionType: FocusSessionType = FocusSessionType.POMODORO
    private var totalDurationMinutes: Int = 25

    inner class LocalBinder : Binder() {
        fun getService(): FocusSessionService = this@FocusSessionService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val type = intent.getStringExtra(EXTRA_SESSION_TYPE)?.let {
                    FocusSessionType.valueOf(it)
                } ?: FocusSessionType.POMODORO
                val duration = intent.getIntExtra(EXTRA_DURATION_MINUTES, 25)
                startFocusSession(type, duration)
            }
            ACTION_STOP -> stopFocusSession(false)
            ACTION_COMPLETE -> stopFocusSession(true)
        }
        return START_NOT_STICKY
    }

    private fun startFocusSession(type: FocusSessionType, durationMinutes: Int) {
        isFocusActive = true
        currentSessionType = type
        totalDurationMinutes = durationMinutes
        val totalSeconds = durationMinutes * 60
        _remainingSeconds.value = totalSeconds
        _isRunning.value = true

        val notification = notificationHelper.buildFocusNotification("${durationMinutes}:00", type.label)
        ServiceCompat.startForeground(
            this, NotificationHelper.NOTIF_FOCUS_ID, notification,
            android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC,
        )

        serviceScope.launch {
            currentSessionId = focusRepository.startSession(type, durationMinutes)
            startTimer(totalSeconds)
        }
    }

    private fun startTimer(totalSeconds: Int) {
        timerJob?.cancel()
        timerJob = serviceScope.launch {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            var remaining = totalSeconds
            while (remaining > 0 && _isRunning.value) {
                delay(1000)
                remaining--
                _remainingSeconds.value = remaining
                // Update notification every 10 seconds to avoid excessive updates
                if (remaining % 10 == 0 || remaining <= 10) {
                    val formatted = "%02d:%02d".format(remaining / 60, remaining % 60)
                    val notification = notificationHelper.buildFocusNotification(formatted, currentSessionType.label)
                    notificationManager.notify(NotificationHelper.NOTIF_FOCUS_ID, notification)
                }
            }
            if (remaining == 0) stopFocusSession(true)
        }
    }

    private fun stopFocusSession(completed: Boolean) {
        isFocusActive = false
        timerJob?.cancel()
        _isRunning.value = false
        if (currentSessionId != -1L) {
            serviceScope.launch {
                if (completed) {
                    focusRepository.completeSession(currentSessionId, totalDurationMinutes)
                    gamificationRepository.awardFocusSession(totalDurationMinutes)
                }
            }
        }
        notificationHelper.cancelFocusNotification()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "com.touchgrass.FOCUS_START"
        const val ACTION_STOP = "com.touchgrass.FOCUS_STOP"
        const val ACTION_COMPLETE = "com.touchgrass.FOCUS_COMPLETE"
        const val EXTRA_SESSION_TYPE = "session_type"
        const val EXTRA_DURATION_MINUTES = "duration_minutes"

        @Volatile var isFocusActive: Boolean = false
            private set
    }
}
