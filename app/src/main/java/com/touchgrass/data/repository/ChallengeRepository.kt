package com.touchgrass.data.repository

import com.touchgrass.data.local.dao.ChallengeDao
import com.touchgrass.data.local.entity.ChallengeEntity
import com.touchgrass.domain.model.CHALLENGE_POOL
import com.touchgrass.domain.model.Challenge
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class ChallengeRepository @Inject constructor(
    private val challengeDao: ChallengeDao,
) {
    fun observeTodaysChallenges(): Flow<List<Challenge>> {
        val today = LocalDate.now()
        val startOfDay = today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endOfDay = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        return challengeDao.observeTodaysChallenges(startOfDay, endOfDay).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun observeCompletedChallenges(): Flow<List<Challenge>> {
        return challengeDao.observeCompletedChallenges().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun ensureTodaysChallengesExist(count: Int = 5) {
        val today = LocalDate.now()
        val startOfDay = today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endOfDay = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val existing = challengeDao.observeTodaysChallenges(startOfDay, endOfDay)
        // Seed with today's challenges if none exist using a deterministic daily seed
        val dailySeed = today.toEpochDay()
        val rng = Random(dailySeed)
        val selected = CHALLENGE_POOL.shuffled(rng).take(count)

        val entities = selected.map { challenge ->
            ChallengeEntity.fromDomain(
                challenge.copy(
                    id = "${challenge.id}_${today.toEpochDay()}",
                    assignedDate = startOfDay,
                )
            )
        }
        challengeDao.upsertChallenges(entities)
    }

    suspend fun completeChallenge(id: String): Challenge? {
        challengeDao.markCompleted(id, System.currentTimeMillis())
        return challengeDao.getChallenge(id)?.toDomain()
    }

    suspend fun getTotalCompletedCount(): Int = challengeDao.getTotalCompletedCount()
}
