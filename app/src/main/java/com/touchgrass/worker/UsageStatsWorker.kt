package com.touchgrass.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.touchgrass.data.repository.GamificationRepository
import com.touchgrass.data.repository.UsageStatsRepository
import com.touchgrass.data.datastore.UserPreferencesDataStore
import com.touchgrass.utils.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

@HiltWorker
class UsageStatsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val usageStatsRepository: UsageStatsRepository,
    private val gamificationRepository: GamificationRepository,
    private val preferencesDataStore: UserPreferencesDataStore,
    private val notificationHelper: NotificationHelper,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            usageStatsRepository.syncTodayUsage()
            val summary = usageStatsRepository.observeTodayUsage().first()
            val prefs = preferencesDataStore.userPreferences.first()

            gamificationRepository.updatePetFromScreenTime(
                summary.totalScreenTimeMs, prefs.dailyGoalMinutes * 60_000L
            )

            val goalMs = prefs.dailyGoalMinutes * 60_000L
            if (summary.totalScreenTimeMs >= goalMs) {
                notificationHelper.sendScreenTimeAlert(
                    summary.totalScreenTimeFormatted, prefs.notificationStyle.name
                )
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "usage_stats_sync"

        fun buildRequest(): PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<UsageStatsWorker>(15, TimeUnit.MINUTES)
                .setConstraints(Constraints.Builder().build())
                .setBackoffCriteria(BackoffPolicy.LINEAR, 5, TimeUnit.MINUTES)
                .build()
    }
}
