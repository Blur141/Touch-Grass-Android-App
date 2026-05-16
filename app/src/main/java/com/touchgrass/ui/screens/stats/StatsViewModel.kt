package com.touchgrass.ui.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.touchgrass.data.repository.GamificationRepository
import com.touchgrass.data.repository.UsageStatsRepository
import com.touchgrass.domain.model.Achievement
import com.touchgrass.domain.model.DailyUsageSummary
import com.touchgrass.domain.model.UserProfile
import com.touchgrass.utils.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StatsUiState(
    val profile: UserProfile = UserProfile(),
    val weeklyData: List<DailyUsageSummary> = emptyList(),
    val unlockedAchievements: List<Achievement> = emptyList(),
    val allAchievements: List<Achievement> = emptyList(),
    val weeklyChartData: List<Pair<String, Float>> = emptyList(),
    val weeklyAvgMs: Long = 0L,
    val bestDayMs: Long = 0L,
    val totalScreenHours: Float = 0f,
)

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val usageStatsRepository: UsageStatsRepository,
    private val gamificationRepository: GamificationRepository,
) : ViewModel() {

    val uiState: StateFlow<StatsUiState> = combine(
        usageStatsRepository.observeWeeklyData(),
        gamificationRepository.observeProfile(),
        gamificationRepository.observeAchievements(),
    ) { weeklyData, profile, achievements ->
        val chartData = weeklyData.map { summary ->
            TimeUtils.epochDayToDisplayDate(summary.date) to summary.totalScreenTimeMs.toFloat()
        }
        val maxMs = weeklyData.maxOfOrNull { it.totalScreenTimeMs } ?: 1L
        StatsUiState(
            profile = profile,
            weeklyData = weeklyData,
            unlockedAchievements = achievements.filter { it.isUnlocked },
            allAchievements = achievements,
            weeklyChartData = chartData,
            weeklyAvgMs = if (weeklyData.isNotEmpty()) weeklyData.sumOf { it.totalScreenTimeMs } / weeklyData.size else 0L,
            bestDayMs = weeklyData.minOfOrNull { it.totalScreenTimeMs } ?: 0L,
            totalScreenHours = weeklyData.sumOf { it.totalScreenTimeMs }.toFloat() / 3_600_000f,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        StatsUiState(),
    )

    init {
        viewModelScope.launch { usageStatsRepository.syncTodayUsage() }
    }
}
