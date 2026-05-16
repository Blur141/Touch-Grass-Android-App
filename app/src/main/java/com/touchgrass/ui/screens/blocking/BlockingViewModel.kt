package com.touchgrass.ui.screens.blocking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.touchgrass.data.repository.GamificationRepository
import com.touchgrass.service.AppBlockingService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BlockingUiState(
    val cooldownSeconds: Int = 30,
    val isBypassReady: Boolean = false,
    val bypassUsed: Boolean = false,
    val xpPenalty: Int = 50,
)

@HiltViewModel
class BlockingViewModel @Inject constructor(
    private val gamificationRepository: GamificationRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val blockedPackage: String = savedStateHandle.get<String>("packageName") ?: ""

    private val _uiState = MutableStateFlow(BlockingUiState())
    val uiState: StateFlow<BlockingUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun startCooldown() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var remaining = 30
            while (remaining > 0) {
                delay(1000)
                remaining--
                _uiState.value = _uiState.value.copy(cooldownSeconds = remaining)
            }
            _uiState.value = _uiState.value.copy(isBypassReady = true)
        }
    }

    fun useEmergencyBypass(packageName: String) {
        viewModelScope.launch {
            gamificationRepository.awardXp(-_uiState.value.xpPenalty)
            AppBlockingService.addBypass(packageName)
            _uiState.value = _uiState.value.copy(bypassUsed = true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
