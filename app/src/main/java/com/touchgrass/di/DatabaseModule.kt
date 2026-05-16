package com.touchgrass.di

import android.content.Context
import androidx.room.Room
import com.touchgrass.data.local.database.AppDatabase
import com.touchgrass.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideUserProfileDao(db: AppDatabase): UserProfileDao = db.userProfileDao()
    @Provides fun provideUsageStatDao(db: AppDatabase): UsageStatDao = db.usageStatDao()
    @Provides fun provideChallengeDao(db: AppDatabase): ChallengeDao = db.challengeDao()
    @Provides fun provideAchievementDao(db: AppDatabase): AchievementDao = db.achievementDao()
    @Provides fun provideFocusSessionDao(db: AppDatabase): FocusSessionDao = db.focusSessionDao()
}
