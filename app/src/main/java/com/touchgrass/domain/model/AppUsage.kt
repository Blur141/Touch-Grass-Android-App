package com.touchgrass.domain.model

data class AppUsage(
    val packageName: String,
    val appName: String,
    val totalTimeMs: Long,
    val launchCount: Int,
    val lastUsed: Long,
    val category: AppCategory = AppCategory.OTHER,
    val isBlocked: Boolean = false,
) {
    val totalTimeMinutes: Int get() = (totalTimeMs / 60_000).toInt()
    val totalTimeFormatted: String get() {
        val hours = totalTimeMs / 3_600_000
        val minutes = (totalTimeMs % 3_600_000) / 60_000
        return when {
            hours > 0 -> "${hours}h ${minutes}m"
            else -> "${minutes}m"
        }
    }
}

enum class AppCategory {
    SOCIAL_MEDIA, VIDEO, GAMING, PRODUCTIVITY, ENTERTAINMENT, COMMUNICATION, OTHER;

    val isDistraction: Boolean get() = this in listOf(SOCIAL_MEDIA, VIDEO, GAMING, ENTERTAINMENT)
}

data class DailyUsageSummary(
    val date: Long,
    val totalScreenTimeMs: Long,
    val unlockCount: Int,
    val longestSessionMs: Long,
    val appUsages: List<AppUsage> = emptyList(),
    val dailyGoalMs: Long = 7_200_000L,
) {
    val goalProgress: Float get() = (totalScreenTimeMs.toFloat() / dailyGoalMs).coerceAtMost(1f)
    val isOverGoal: Boolean get() = totalScreenTimeMs > dailyGoalMs
    val totalScreenTimeFormatted: String get() {
        val hours = totalScreenTimeMs / 3_600_000
        val minutes = (totalScreenTimeMs % 3_600_000) / 60_000
        return when {
            hours > 0 -> "${hours}h ${minutes}m"
            else -> "${minutes}m"
        }
    }
    val touchGrassScore: Int get() {
        val baseScore = 100 - ((totalScreenTimeMs.toFloat() / dailyGoalMs) * 100).toInt()
        return baseScore.coerceIn(0, 100)
    }
}
