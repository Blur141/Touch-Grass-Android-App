package com.touchgrass.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.touchgrass.data.datastore.UserPreferencesDataStore
import com.touchgrass.data.repository.ChallengeRepository
import com.touchgrass.data.repository.GamificationRepository
import com.touchgrass.data.repository.UsageStatsRepository
import com.touchgrass.domain.model.*
import com.touchgrass.utils.PermissionUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val profile: UserProfile = UserProfile(),
    val dailySummary: DailyUsageSummary = DailyUsageSummary(0, 0, 0, 0),
    val todaysChallenges: List<Challenge> = emptyList(),
    val topApps: List<AppUsage> = emptyList(),
    val dailyGoalMs: Long = 7_200_000L,
    val recentAchievement: Achievement? = null,
    val hasUsageStatsPermission: Boolean = true,
    val hasOverlayPermission: Boolean = true,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val usageStatsRepository: UsageStatsRepository,
    private val gamificationRepository: GamificationRepository,
    private val challengeRepository: ChallengeRepository,
    private val preferencesDataStore: UserPreferencesDataStore,
    private val permissionUtils: PermissionUtils,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                hasUsageStatsPermission = permissionUtils.hasUsageStatsPermission(),
                hasOverlayPermission = permissionUtils.hasOverlayPermission(),
            )
        }
        viewModelScope.launch {
            gamificationRepository.ensureProfileExists()
            gamificationRepository.ensureAchievementsSeeded()
            challengeRepository.ensureTodaysChallengesExist()
            usageStatsRepository.syncTodayUsage()
        }
        observeData()
    }

    fun recheckPermissions() {
        _uiState.update {
            it.copy(
                hasUsageStatsPermission = permissionUtils.hasUsageStatsPermission(),
                hasOverlayPermission = permissionUtils.hasOverlayPermission(),
            )
        }
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(
                gamificationRepository.observeProfile(),
                usageStatsRepository.observeTodayUsage(),
                challengeRepository.observeTodaysChallenges(),
                preferencesDataStore.userPreferences,
            ) { profile, summary, challenges, prefs ->
                val goalMs = prefs.dailyGoalMinutes * 60_000L
                HomeUiState(
                    isLoading = false,
                    profile = profile,
                    dailySummary = summary.copy(dailyGoalMs = goalMs),
                    todaysChallenges = challenges,
                    topApps = summary.appUsages.take(3),
                    dailyGoalMs = goalMs,
                )
            }.collect { state ->
                _uiState.value = state.copy(
                    hasUsageStatsPermission = permissionUtils.hasUsageStatsPermission()
                )
            }
        }
    }

    fun completeChallenge(challengeId: String) {
        viewModelScope.launch {
            val challenge = challengeRepository.completeChallenge(challengeId)
            if (challenge != null) {
                gamificationRepository.completeChallengeXp(challenge.xpReward)
            }
        }
    }

    fun refreshUsageStats() {
        viewModelScope.launch {
            usageStatsRepository.syncTodayUsage()
        }
    }
}
