package com.touchgrass.data.local.dao

import androidx.room.*
import com.touchgrass.data.local.entity.AppUsageEntity
import com.touchgrass.data.local.entity.DailyUsageSummaryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsageStatDao {
    @Query("SELECT * FROM daily_usage_summary WHERE dateEpochDay = :dateEpochDay")
    fun observeDailySummary(dateEpochDay: Long): Flow<DailyUsageSummaryEntity?>

    @Query("SELECT * FROM daily_usage_summary ORDER BY dateEpochDay DESC LIMIT :limit")
    fun observeRecentSummaries(limit: Int = 30): Flow<List<DailyUsageSummaryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDailySummary(summary: DailyUsageSummaryEntity)

    @Query("SELECT * FROM app_usage WHERE dateEpochDay = :dateEpochDay ORDER BY totalTimeMs DESC")
    fun observeAppUsageForDay(dateEpochDay: Long): Flow<List<AppUsageEntity>>

    @Query("SELECT * FROM app_usage WHERE dateEpochDay = :dateEpochDay ORDER BY totalTimeMs DESC")
    suspend fun getAppUsageForDay(dateEpochDay: Long): List<AppUsageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAppUsages(usages: List<AppUsageEntity>)

    @Query("SELECT SUM(totalScreenTimeMs) FROM daily_usage_summary WHERE dateEpochDay >= :startDay AND dateEpochDay <= :endDay")
    suspend fun getTotalScreenTimeInRange(startDay: Long, endDay: Long): Long?

    @Query("SELECT * FROM daily_usage_summary WHERE dateEpochDay >= :startDay ORDER BY dateEpochDay ASC")
    fun observeSummariesSince(startDay: Long): Flow<List<DailyUsageSummaryEntity>>
}
