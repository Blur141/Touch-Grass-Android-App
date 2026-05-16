package com.touchgrass.ui.screens.settings

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.touchgrass.data.datastore.NotificationStyle
import com.touchgrass.data.datastore.UserPreferencesDataStore
import com.touchgrass.data.repository.GamificationRepository
import com.touchgrass.service.SleepAlarmReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InstalledApp(
    val packageName: String,
    val appName: String,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesDataStore: UserPreferencesDataStore,
    private val gamificationRepository: GamificationRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    val preferences = preferencesDataStore.userPreferences.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), com.touchgrass.data.datastore.UserPreferences()
    )

    val profileName: StateFlow<String> = gamificationRepository.observeProfile()
        .map { it.name }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    private val _installedApps = MutableStateFlow<List<InstalledApp>>(emptyList())
    val installedApps: StateFlow<List<InstalledApp>> = _installedApps.asStateFlow()

    private val _appSearchQuery = MutableStateFlow("")
    val appSearchQuery: StateFlow<String> = _appSearchQuery.asStateFlow()

    private val _nameEditValue = MutableStateFlow("")
    val nameEditValue: StateFlow<String> = _nameEditValue.asStateFlow()

    init {
        loadInstalledApps()
    }

    private fun loadInstalledApps() {
        viewModelScope.launch(Dispatchers.IO) {
            val pm = context.packageManager
            val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
                .filter { app ->
                    app.flags and ApplicationInfo.FLAG_SYSTEM == 0 &&
                        app.packageName != context.packageName
                }
                .map { app ->
                    InstalledApp(
                        packageName = app.packageName,
                        appName = pm.getApplicationLabel(app).toString(),
                    )
                }
                .sortedBy { it.appName }
            _installedApps.value = apps
        }
    }

    fun setAppSearchQuery(query: String) { _appSearchQuery.value = query }

    fun setNameEditValue(value: String) { _nameEditValue.value = value }

    fun saveName(name: String) = viewModelScope.launch {
        gamificationRepository.updateUserName(name)
        _nameEditValue.value = ""
    }

    fun setDailyGoal(minutes: Int) = viewModelScope.launch {
        preferencesDataStore.setDailyGoalMinutes(minutes)
    }

    fun setDarkMode(enabled: Boolean) = viewModelScope.launch {
        preferencesDataStore.setDarkMode(enabled)
    }

    fun setHapticEnabled(enabled: Boolean) = viewModelScope.launch {
        preferencesDataStore.setHapticEnabled(enabled)
    }

    fun setNotificationStyle(style: NotificationStyle) = viewModelScope.launch {
        preferencesDataStore.setNotificationStyle(style)
    }

    fun toggleBlockedApp(packageName: String) = viewModelScope.launch {
        val current = preferences.value.blockedApps.toMutableSet()
        if (packageName in current) current.remove(packageName) else current.add(packageName)
        preferencesDataStore.setBlockedApps(current)
    }

    fun setShowPetNotifications(show: Boolean) = viewModelScope.launch {
        preferencesDataStore.setShowPetNotifications(show)
    }

    fun setSleepEnabled(enabled: Boolean) = viewModelScope.launch {
        preferencesDataStore.setSleepEnabled(enabled)
        val prefs = preferences.value
        if (enabled) {
            SleepAlarmReceiver.scheduleAlarm(context, prefs.bedtimeHour, prefs.bedtimeMinute, SleepAlarmReceiver.ACTION_SLEEP, 501)
            SleepAlarmReceiver.scheduleAlarm(context, prefs.wakeHour, prefs.wakeMinute, SleepAlarmReceiver.ACTION_WAKE, 502)
        } else {
            SleepAlarmReceiver.cancelAlarm(context, SleepAlarmReceiver.ACTION_SLEEP, 501)
            SleepAlarmReceiver.cancelAlarm(context, SleepAlarmReceiver.ACTION_WAKE, 502)
        }
    }

    fun setBedtime(hour: Int, minute: Int) = viewModelScope.launch {
        preferencesDataStore.setBedtime(hour, minute)
        if (preferences.value.sleepEnabled) {
            SleepAlarmReceiver.scheduleAlarm(context, hour, minute, SleepAlarmReceiver.ACTION_SLEEP, 501)
        }
    }

    fun setWakeTime(hour: Int, minute: Int) = viewModelScope.launch {
        preferencesDataStore.setWakeTime(hour, minute)
        if (preferences.value.sleepEnabled) {
            SleepAlarmReceiver.scheduleAlarm(context, hour, minute, SleepAlarmReceiver.ACTION_WAKE, 502)
        }
    }
}
