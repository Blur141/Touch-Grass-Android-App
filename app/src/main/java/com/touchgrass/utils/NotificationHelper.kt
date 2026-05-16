package com.touchgrass.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.touchgrass.MainActivity
import com.touchgrass.service.SleepAlarmReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManager,
) {
    companion object {
        const val CHANNEL_TRACKING = "touch_grass_tracking"
        const val CHANNEL_FOCUS = "touch_grass_focus"
        const val CHANNEL_CHALLENGE = "touch_grass_challenge"
        const val CHANNEL_PET = "touch_grass_pet"
        const val CHANNEL_BLOCKING = "touch_grass_blocking"

        const val NOTIF_FOCUS_ID = 1001
        const val NOTIF_BLOCKING_ID = 1002
        const val NOTIF_BLOCKING_OVERLAY_ID = 1003

        private val FUNNY_MESSAGES = listOf(
            "Bro you've been scrolling for 2 hours 💀",
            "Your plant misses sunlight. And you.",
            "Go outside. The graphics are insane.",
            "You survived 30 mins without TikTok. The world didn't end.",
            "Your thumbs need a break. The rest of you too.",
            "Hot take: grass exists outside your screen.",
            "Breaking: local human forgets what sky looks like.",
            "This is your sign to touch grass. Literally.",
            "The sunset is free. Your data plan is not.",
            "Reality called. It has better resolution.",
            "Achievement unlocked: Remembering outside exists.",
            "Your phone has no nutritional value. Food does.",
            "Skill issue: touching real grass.",
            "Sir, this is a planet Earth. Please go visit it.",
        )

        private val MOTIVATIONAL_MESSAGES = listOf(
            "Every minute offline is a victory.",
            "You're building a better relationship with your phone.",
            "Your future self is proud of you.",
            "Small breaks lead to big changes.",
            "The real world has been waiting for you.",
        )
    }

    fun createChannels() {
        val channels = listOf(
            NotificationChannel(CHANNEL_TRACKING, "Screen Time Alerts", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Alerts when you exceed your screen time goal"
            },
            NotificationChannel(CHANNEL_FOCUS, "Focus Mode", NotificationManager.IMPORTANCE_LOW).apply {
                description = "Focus session status and updates"
            },
            NotificationChannel(CHANNEL_CHALLENGE, "Daily Challenges", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Challenge reminders and completions"
            },
            NotificationChannel(CHANNEL_PET, "Your Plant", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Updates about your virtual plant"
            },
            NotificationChannel(SleepAlarmReceiver.CHANNEL_SLEEP, "Sleep Schedule", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Bedtime and wake-up reminders"
            },
            NotificationChannel(CHANNEL_BLOCKING, "App Blocking", NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Alerts when a blocked app is opened"
            },
        )
        channels.forEach { notificationManager.createNotificationChannel(it) }
    }

    fun buildFocusNotification(timeRemaining: String, sessionName: String): Notification {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(context, CHANNEL_FOCUS)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("$sessionName in progress ⚡")
            .setContentText("$timeRemaining remaining. You've got this.")
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setSilent(true)
            .build()
    }

    fun sendScreenTimeAlert(screenTimeFormatted: String, style: String = "FUNNY") {
        val message = if (style == "FUNNY") FUNNY_MESSAGES.random() else MOTIVATIONAL_MESSAGES.random()
        val notification = NotificationCompat.Builder(context, CHANNEL_TRACKING)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Screen Time: $screenTimeFormatted")
            .setContentText(message)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(2001, notification)
    }

    fun sendChallengeNotification(challengeTitle: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_CHALLENGE)
            .setSmallIcon(android.R.drawable.ic_menu_compass)
            .setContentTitle("New challenge: $challengeTitle 🌿")
            .setContentText("Complete it to earn XP and keep your streak alive.")
            .setAutoCancel(true)
            .build()
        notificationManager.notify(3001, notification)
    }

    fun sendPetAlert(message: String) {
        val notification = NotificationCompat.Builder(context, CHANNEL_PET)
            .setSmallIcon(android.R.drawable.ic_menu_myplaces)
            .setContentTitle("Your plant needs you 🌱")
            .setContentText(message)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(4001, notification)
    }

    fun cancelFocusNotification() {
        notificationManager.cancel(NOTIF_FOCUS_ID)
    }
}
