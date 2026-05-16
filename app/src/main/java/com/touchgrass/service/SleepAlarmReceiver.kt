package com.touchgrass.service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import java.util.Calendar

class SleepAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        ensureChannel(nm)

        val isSleep = intent.action == ACTION_SLEEP
        val quotes = if (isSleep) SLEEP_QUOTES else WAKE_QUOTES
        val quote = quotes.random()

        val notification = NotificationCompat.Builder(context, CHANNEL_SLEEP)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(if (isSleep) "Time for bed 😴" else "Rise and shine! ☀️")
            .setContentText(quote)
            .setStyle(NotificationCompat.BigTextStyle().bigText(quote))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        nm.notify(if (isSleep) NOTIF_SLEEP_ID else NOTIF_WAKE_ID, notification)

        // Reschedule for next day (exact alarms don't repeat automatically)
        scheduleNextDay(context, intent.action ?: return)
    }

    private fun ensureChannel(nm: NotificationManager) {
        if (nm.getNotificationChannel(CHANNEL_SLEEP) == null) {
            val channel = NotificationChannel(
                CHANNEL_SLEEP, "Sleep Schedule", NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "Bedtime and wake-up reminders" }
            nm.createNotificationChannel(channel)
        }
    }

    companion object {
        const val ACTION_SLEEP = "com.touchgrass.SLEEP_ALARM"
        const val ACTION_WAKE = "com.touchgrass.WAKE_ALARM"
        const val CHANNEL_SLEEP = "touch_grass_sleep"
        const val NOTIF_SLEEP_ID = 5001
        const val NOTIF_WAKE_ID = 5002

        private val SLEEP_QUOTES = listOf(
            "Put the phone down. Your plant needs you well-rested.",
            "Tomorrow's grass won't touch itself. Sleep up.",
            "Your brain is calling. Send it to voicemail and sleep.",
            "The best productivity hack is 8 hours of sleep.",
            "No more scrolling. The algorithm will survive without you.",
            "Your future self is begging you to go to sleep right now.",
        )

        private val WAKE_QUOTES = listOf(
            "Rise and shine! Start your day screen-free.",
            "The grass is dewy. Go touch it before you touch your phone.",
            "Morning! Your phone will still be there after breakfast.",
            "New day, new chance to actually live in it.",
            "The birds are up. The gym is open. Your phone can wait.",
            "Today is a great day to put the phone down and exist IRL.",
        )

        fun scheduleAlarm(context: Context, hour: Int, minute: Int, action: String, reqCode: Int) {
            val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, SleepAlarmReceiver::class.java).apply { this.action = action }
            val pi = PendingIntent.getBroadcast(
                context, reqCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                if (before(Calendar.getInstance())) add(Calendar.DAY_OF_YEAR, 1)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
            } else {
                am.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
            }
        }

        fun cancelAlarm(context: Context, action: String, reqCode: Int) {
            val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, SleepAlarmReceiver::class.java).apply { this.action = action }
            val pi = PendingIntent.getBroadcast(
                context, reqCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
            am.cancel(pi)
        }

        private fun scheduleNextDay(context: Context, action: String) {
            val reqCode = if (action == ACTION_SLEEP) 501 else 502
            val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, SleepAlarmReceiver::class.java).apply { this.action = action }
            val pi = PendingIntent.getBroadcast(
                context, reqCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
            // Add 24 hours from now
            val nextTrigger = System.currentTimeMillis() + 86_400_000L
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextTrigger, pi)
            } else {
                am.setExact(AlarmManager.RTC_WAKEUP, nextTrigger, pi)
            }
        }
    }
}
