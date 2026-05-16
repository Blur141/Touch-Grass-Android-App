package com.touchgrass.ui.screens.focus

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.touchgrass.domain.model.FocusSessionType
import com.touchgrass.domain.model.FocusState
import com.touchgrass.service.FocusSessionService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FocusViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _focusState = MutableStateFlow(FocusState())
    val focusState: StateFlow<FocusState> = _focusState.asStateFlow()

    private val _selectedType = MutableStateFlow(FocusSessionType.POMODORO)
    val selectedType: StateFlow<FocusSessionType> = _selectedType.asStateFlow()

    private val _customMinutes = MutableStateFlow(30)
    val customMinutes: StateFlow<Int> = _customMinutes.asStateFlow()

    private var focusService: FocusSessionService? = null
    private var isBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val localBinder = binder as? FocusSessionService.LocalBinder ?: return
            focusService = localBinder.getService()
            isBound = true

            viewModelScope.launch {
                focusService?.remainingSeconds?.collect { remaining ->
                    val type = _selectedType.value
                    val total = getDurationMinutes(type) * 60
                    _focusState.value = _focusState.value.copy(
                        remainingSeconds = remaining,
                        totalSeconds = total,
                        isActive = focusService?.isRunning?.value == true,
                    )
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            focusService = null
            isBound = false
        }
    }

    fun startSession() {
        val type = _selectedType.value
        val duration = getDurationMinutes(type)

        val intent = Intent(context, FocusSessionService::class.java).apply {
            action = FocusSessionService.ACTION_START
            putExtra(FocusSessionService.EXTRA_SESSION_TYPE, type.name)
            putExtra(FocusSessionService.EXTRA_DURATION_MINUTES, duration)
        }
        context.startForegroundService(intent)

        context.bindService(
            Intent(context, FocusSessionService::class.java),
            connection, Context.BIND_AUTO_CREATE,
        )

        _focusState.value = FocusState(
            isActive = true,
            sessionType = type,
            totalSeconds = duration * 60,
            remainingSeconds = duration * 60,
        )
    }

    fun stopSession() {
        val intent = Intent(context, FocusSessionService::class.java).apply {
            action = FocusSessionService.ACTION_STOP
        }
        context.startService(intent)
        _focusState.value = FocusState()
        if (isBound) {
            context.unbindService(connection)
            isBound = false
        }
    }

    fun selectSessionType(type: FocusSessionType) {
        _selectedType.value = type
        val duration = getDurationMinutes(type)
        _focusState.value = FocusState(
            sessionType = type,
            totalSeconds = duration * 60,
            remainingSeconds = duration * 60,
        )
    }

    fun setCustomMinutes(minutes: Int) {
        _customMinutes.value = minutes.coerceIn(5, 180)
    }

    private fun getDurationMinutes(type: FocusSessionType): Int =
        if (type == FocusSessionType.CUSTOM) _customMinutes.value else type.defaultMinutes

    override fun onCleared() {
        super.onCleared()
        if (isBound) {
            context.unbindService(connection)
            isBound = false
        }
    }
}
