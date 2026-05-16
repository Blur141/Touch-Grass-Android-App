package com.touchgrass.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.touchgrass.data.local.dao.*
import com.touchgrass.data.local.entity.*

@Database(
    entities = [
        UserProfileEntity::class,
        DailyUsageSummaryEntity::class,
        AppUsageEntity::class,
        ChallengeEntity::class,
        AchievementEntity::class,
        FocusSessionEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun usageStatDao(): UsageStatDao
    abstract fun challengeDao(): ChallengeDao
    abstract fun achievementDao(): AchievementDao
    abstract fun focusSessionDao(): FocusSessionDao

    companion object {
        const val DATABASE_NAME = "touch_grass_db"
    }
}
