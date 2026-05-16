package com.touchgrass.data.repository

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import com.touchgrass.data.local.dao.UsageStatDao
import com.touchgrass.data.local.entity.AppUsageEntity
import com.touchgrass.data.local.entity.DailyUsageSummaryEntity
import com.touchgrass.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsageStatsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val usageStatDao: UsageStatDao,
) {
    private val usageStatsManager by lazy {
        context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    }

    private val packageManager: PackageManager get() = context.packageManager

    private val distractingPackages = setOf(
        "com.instagram.android", "com.zhiliaoapp.musically", "com.google.android.youtube",
        "com.reddit.frontpage", "com.twitter.android", "com.facebook.katana",
        "com.snapchat.android", "com.pinterest", "com.tumblr",
    )

    fun observeTodayUsage(): Flow<DailyUsageSummary> {
        val today = LocalDate.now()
        val epochDay = today.toEpochDay()
        return usageStatDao.observeDailySummary(epochDay).combine(
            usageStatDao.observeAppUsageForDay(epochDay)
        ) { summary, apps ->
            summary?.toDomain(apps.map { it.toDomain() }) ?: DailyUsageSummary(
                date = epochDay, totalScreenTimeMs = 0L, unlockCount = 0, longestSessionMs = 0L,
            )
        }
    }

    fun observeWeeklyData(): Flow<List<DailyUsageSummary>> {
        val sevenDaysAgo = LocalDate.now().minusDays(7).toEpochDay()
        return usageStatDao.observeSummariesSince(sevenDaysAgo).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun syncTodayUsage() {
        val today = LocalDate.now()
        val startMs = today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endMs = System.currentTimeMillis()

        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, startMs, endMs
        ) ?: return

        val appUsages = mutableListOf<AppUsage>()
        var totalTimeMs = 0L
        var longestSessionMs = 0L

        stats.forEach { stat ->
            if (stat.totalTimeInForeground > 0) {
                val appName = runCatching {
                    val info = packageManager.getApplicationInfo(stat.packageName, 0)
                    packageManager.getApplicationLabel(info).toString()
                }.getOrDefault(stat.packageName)

                val category = categorizeApp(stat.packageName)
                totalTimeMs += stat.totalTimeInForeground
                longestSessionMs = maxOf(longestSessionMs, stat.totalTimeInForeground)

                appUsages.add(AppUsage(
                    packageName = stat.packageName, appName = appName,
                    totalTimeMs = stat.totalTimeInForeground,
                    launchCount = stat.lastTimeUsed.toInt(),
                    lastUsed = stat.lastTimeUsed, category = category,
                    isBlocked = stat.packageName in distractingPackages,
                ))
            }
        }

        val epochDay = today.toEpochDay()
        usageStatDao.upsertDailySummary(
            DailyUsageSummaryEntity(
                dateEpochDay = epochDay, totalScreenTimeMs = totalTimeMs,
                unlockCount = estimateUnlockCount(stats.size), longestSessionMs = longestSessionMs,
                dailyGoalMs = 7_200_000L,
            )
        )
        usageStatDao.upsertAppUsages(
            appUsages.sortedByDescending { it.totalTimeMs }
                .take(20)
                .map { AppUsageEntity.fromDomain(it, epochDay) }
        )
    }

    private fun categorizeApp(packageName: String): AppCategory = when {
        packageName.contains("instagram") || packageName.contains("tiktok") ||
        packageName.contains("twitter") || packageName.contains("facebook") ||
        packageName.contains("snapchat") || packageName.contains("pinterest") -> AppCategory.SOCIAL_MEDIA
        packageName.contains("youtube") || packageName.contains("netflix") ||
        packageName.contains("twitch") || packageName.contains("spotify") -> AppCategory.VIDEO
        packageName.contains("game") || packageName.contains("play") -> AppCategory.GAMING
        packageName.contains("gmail") || packageName.contains("messages") ||
        packageName.contains("whatsapp") || packageName.contains("telegram") -> AppCategory.COMMUNICATION
        packageName.contains("docs") || packageName.contains("sheets") ||
        packageName.contains("notion") || packageName.contains("calendar") -> AppCategory.PRODUCTIVITY
        else -> AppCategory.OTHER
    }

    private fun estimateUnlockCount(statCount: Int): Int = (statCount * 1.5).toInt().coerceAtLeast(1)
}
