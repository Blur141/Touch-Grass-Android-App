package com.touchgrass.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.touchgrass.domain.model.AppCategory
import com.touchgrass.domain.model.AppUsage
import com.touchgrass.domain.model.DailyUsageSummary

@Entity(tableName = "daily_usage_summary")
data class DailyUsageSummaryEntity(
    @PrimaryKey val dateEpochDay: Long,
    val totalScreenTimeMs: Long,
    val unlockCount: Int,
    val longestSessionMs: Long,
    val dailyGoalMs: Long,
) {
    fun toDomain(apps: List<AppUsage> = emptyList()) = DailyUsageSummary(
        date = dateEpochDay,
        totalScreenTimeMs = totalScreenTimeMs,
        unlockCount = unlockCount,
        longestSessionMs = longestSessionMs,
        appUsages = apps,
        dailyGoalMs = dailyGoalMs,
    )
}

@Entity(tableName = "app_usage", primaryKeys = ["packageName", "dateEpochDay"])
data class AppUsageEntity(
    val packageName: String,
    val appName: String,
    val totalTimeMs: Long,
    val launchCount: Int,
    val lastUsed: Long,
    val dateEpochDay: Long,
    val category: String,
    val isBlocked: Boolean,
) {
    fun toDomain() = AppUsage(
        packageName = packageName, appName = appName, totalTimeMs = totalTimeMs,
        launchCount = launchCount, lastUsed = lastUsed,
        category = runCatching { AppCategory.valueOf(category) }.getOrDefault(AppCategory.OTHER),
        isBlocked = isBlocked,
    )

    companion object {
        fun fromDomain(usage: AppUsage, dateEpochDay: Long) = AppUsageEntity(
            packageName = usage.packageName, appName = usage.appName,
            totalTimeMs = usage.totalTimeMs, launchCount = usage.launchCount,
            lastUsed = usage.lastUsed, dateEpochDay = dateEpochDay,
            category = usage.category.name, isBlocked = usage.isBlocked,
        )
    }
}
