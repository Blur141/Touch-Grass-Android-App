package com.touchgrass.ui.screens.challenges

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.touchgrass.data.datastore.UserPreferencesDataStore
import com.touchgrass.data.repository.ChallengeRepository
import com.touchgrass.data.repository.GamificationRepository
import com.touchgrass.domain.model.Challenge
import com.touchgrass.utils.HapticUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChallengesUiState(
    val todaysChallenges: List<Challenge> = emptyList(),
    val completedChallenges: List<Challenge> = emptyList(),
    val completedCount: Int = 0,
    val totalXpEarned: Int = 0,
    val justCompleted: Challenge? = null,
)

@HiltViewModel
class ChallengesViewModel @Inject constructor(
    private val challengeRepository: ChallengeRepository,
    private val gamificationRepository: GamificationRepository,
    private val hapticUtils: HapticUtils,
    private val preferencesDataStore: UserPreferencesDataStore,
) : ViewModel() {

    val uiState: StateFlow<ChallengesUiState> = combine(
        challengeRepository.observeTodaysChallenges(),
        challengeRepository.observeCompletedChallenges(),
    ) { today, completed ->
        ChallengesUiState(
            todaysChallenges = today,
            completedChallenges = completed.take(10),
            completedCount = completed.size,
            totalXpEarned = completed.sumOf { it.xpReward },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ChallengesUiState())

    private val _justCompleted = MutableStateFlow<Challenge?>(null)
    val justCompleted = _justCompleted.asStateFlow()

    init {
        viewModelScope.launch { challengeRepository.ensureTodaysChallengesExist() }
    }

    fun completeChallenge(id: String) {
        viewModelScope.launch {
            val challenge = challengeRepository.completeChallenge(id)
            if (challenge != null) {
                gamificationRepository.completeChallengeXp(challenge.xpReward)
                _justCompleted.value = challenge
                if (preferencesDataStore.userPreferences.first().isHapticEnabled) {
                    hapticUtils.success()
                }
            }
        }
    }

    fun dismissJustCompleted() {
        _justCompleted.value = null
    }
}
