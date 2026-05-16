package com.touchgrass.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

data class UserPreferences(
    val dailyGoalMinutes: Int = 120,
    val isDarkMode: Boolean = true,
    val isHapticEnabled: Boolean = true,
    val notificationStyle: NotificationStyle = NotificationStyle.FUNNY,
    val blockedApps: Set<String> = setOf("com.instagram.android", "com.zhiliaoapp.musically", "com.google.android.youtube", "com.reddit.frontpage", "com.twitter.android"),
    val isFocusModeActive: Boolean = false,
    val challengeFrequency: Int = 5,
    val onboardingCompleted: Boolean = false,
    val weeklyGoalDays: Int = 5,
    val showPetNotifications: Boolean = true,
    val lastKnownStreak: Int = 0,
    val sleepEnabled: Boolean = false,
    val bedtimeHour: Int = 22,
    val bedtimeMinute: Int = 30,
    val wakeHour: Int = 7,
    val wakeMinute: Int = 0,
)

enum class NotificationStyle { FUNNY, MOTIVATIONAL, MINIMAL }

@Singleton
class UserPreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val dataStore = context.dataStore

    companion object {
        val DAILY_GOAL_MINUTES = intPreferencesKey("daily_goal_minutes")
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val IS_HAPTIC_ENABLED = booleanPreferencesKey("is_haptic_enabled")
        val NOTIFICATION_STYLE = stringPreferencesKey("notification_style")
        val BLOCKED_APPS = stringSetPreferencesKey("blocked_apps")
        val IS_FOCUS_MODE_ACTIVE = booleanPreferencesKey("is_focus_mode_active")
        val CHALLENGE_FREQUENCY = intPreferencesKey("challenge_frequency")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val WEEKLY_GOAL_DAYS = intPreferencesKey("weekly_goal_days")
        val SHOW_PET_NOTIFICATIONS = booleanPreferencesKey("show_pet_notifications")
        val LAST_KNOWN_STREAK = intPreferencesKey("last_known_streak")
        val SLEEP_ENABLED = booleanPreferencesKey("sleep_enabled")
        val BEDTIME_HOUR = intPreferencesKey("bedtime_hour")
        val BEDTIME_MINUTE = intPreferencesKey("bedtime_minute")
        val WAKE_HOUR = intPreferencesKey("wake_hour")
        val WAKE_MINUTE = intPreferencesKey("wake_minute")
    }

    val userPreferences: Flow<UserPreferences> = dataStore.data
        .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
        .map { prefs ->
            UserPreferences(
                dailyGoalMinutes = prefs[DAILY_GOAL_MINUTES] ?: 120,
                isDarkMode = prefs[IS_DARK_MODE] ?: true,
                isHapticEnabled = prefs[IS_HAPTIC_ENABLED] ?: true,
                notificationStyle = runCatching {
                    NotificationStyle.valueOf(prefs[NOTIFICATION_STYLE] ?: "FUNNY")
                }.getOrDefault(NotificationStyle.FUNNY),
                blockedApps = prefs[BLOCKED_APPS] ?: setOf(
                    "com.instagram.android", "com.zhiliaoapp.musically",
                    "com.google.android.youtube", "com.reddit.frontpage", "com.twitter.android"
                ),
                isFocusModeActive = prefs[IS_FOCUS_MODE_ACTIVE] ?: false,
                challengeFrequency = prefs[CHALLENGE_FREQUENCY] ?: 5,
                onboardingCompleted = prefs[ONBOARDING_COMPLETED] ?: false,
                weeklyGoalDays = prefs[WEEKLY_GOAL_DAYS] ?: 5,
                showPetNotifications = prefs[SHOW_PET_NOTIFICATIONS] ?: true,
                lastKnownStreak = prefs[LAST_KNOWN_STREAK] ?: 0,
                sleepEnabled = prefs[SLEEP_ENABLED] ?: false,
                bedtimeHour = prefs[BEDTIME_HOUR] ?: 22,
                bedtimeMinute = prefs[BEDTIME_MINUTE] ?: 30,
                wakeHour = prefs[WAKE_HOUR] ?: 7,
                wakeMinute = prefs[WAKE_MINUTE] ?: 0,
            )
        }

    suspend fun setDailyGoalMinutes(minutes: Int) {
        dataStore.edit { it[DAILY_GOAL_MINUTES] = minutes }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { it[IS_DARK_MODE] = enabled }
    }

    suspend fun setHapticEnabled(enabled: Boolean) {
        dataStore.edit { it[IS_HAPTIC_ENABLED] = enabled }
    }

    suspend fun setNotificationStyle(style: NotificationStyle) {
        dataStore.edit { it[NOTIFICATION_STYLE] = style.name }
    }

    suspend fun setBlockedApps(apps: Set<String>) {
        dataStore.edit { it[BLOCKED_APPS] = apps }
    }

    suspend fun setFocusModeActive(active: Boolean) {
        dataStore.edit { it[IS_FOCUS_MODE_ACTIVE] = active }
    }

    suspend fun setOnboardingCompleted() {
        dataStore.edit { it[ONBOARDING_COMPLETED] = true }
    }

    suspend fun setWeeklyGoalDays(days: Int) {
        dataStore.edit { it[WEEKLY_GOAL_DAYS] = days }
    }

    suspend fun setShowPetNotifications(show: Boolean) {
        dataStore.edit { it[SHOW_PET_NOTIFICATIONS] = show }
    }

    suspend fun setLastKnownStreak(streak: Int) {
        dataStore.edit { it[LAST_KNOWN_STREAK] = streak }
    }

    suspend fun setSleepEnabled(enabled: Boolean) {
        dataStore.edit { it[SLEEP_ENABLED] = enabled }
    }

    suspend fun setBedtime(hour: Int, minute: Int) {
        dataStore.edit { it[BEDTIME_HOUR] = hour; it[BEDTIME_MINUTE] = minute }
    }

    suspend fun setWakeTime(hour: Int, minute: Int) {
        dataStore.edit { it[WAKE_HOUR] = hour; it[WAKE_MINUTE] = minute }
    }
}
