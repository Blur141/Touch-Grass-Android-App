package com.touchgrass.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.touchgrass.data.repository.ChallengeRepository
import com.touchgrass.data.repository.GamificationRepository
import com.touchgrass.data.repository.UsageStatsRepository
import com.touchgrass.data.datastore.UserPreferencesDataStore
import com.touchgrass.utils.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

@HiltWorker
class DailyResetWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val challengeRepository: ChallengeRepository,
    private val gamificationRepository: GamificationRepository,
    private val usageStatsRepository: UsageStatsRepository,
    private val preferencesDataStore: UserPreferencesDataStore,
    private val notificationHelper: NotificationHelper,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            challengeRepository.ensureTodaysChallengesExist()
            usageStatsRepository.syncTodayUsage()

            // Streak: check if user was active yesterday (lastActiveDate within past 24h from midnight)
            val profile = gamificationRepository.observeProfile().first()
            val lastActive = profile.lastActiveDate
            val yesterdayStartMs = LocalDate.now().minusDays(1)
                .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val todayStartMs = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

            val newStreak = when {
                lastActive in yesterdayStartMs until todayStartMs -> profile.streak + 1
                lastActive >= todayStartMs -> profile.streak // Already counted today
                else -> 0 // Missed a day
            }
            gamificationRepository.updateStreak(newStreak)
            preferencesDataStore.setLastKnownStreak(newStreak)

            val challenges = challengeRepository.observeTodaysChallenges().first()
            if (challenges.isNotEmpty()) {
                notificationHelper.sendChallengeNotification(challenges.first().title)
            }

            if (profile.petHealthPoints < 40) {
                notificationHelper.sendPetAlert("Your plant is wilting 😢 Put the phone down and let it grow!")
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "daily_reset"

        fun buildRequest(): PeriodicWorkRequest {
            val now = LocalDateTime.now(ZoneId.systemDefault())
            val nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay(ZoneId.systemDefault())
            val delayMs = nextMidnight.toInstant().toEpochMilli() - System.currentTimeMillis()
            return PeriodicWorkRequestBuilder<DailyResetWorker>(24, TimeUnit.HOURS)
                .setInitialDelay(delayMs.coerceAtLeast(0), TimeUnit.MILLISECONDS)
                .build()
        }
    }
}
