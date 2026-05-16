package com.touchgrass.data.local.dao

import androidx.room.*
import com.touchgrass.data.local.entity.ChallengeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDao {
    @Query("SELECT * FROM challenges WHERE assignedDate >= :startOfDay AND assignedDate < :endOfDay")
    fun observeTodaysChallenges(startOfDay: Long, endOfDay: Long): Flow<List<ChallengeEntity>>

    @Query("SELECT * FROM challenges WHERE isCompleted = 1 ORDER BY completedAt DESC LIMIT :limit")
    fun observeCompletedChallenges(limit: Int = 50): Flow<List<ChallengeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertChallenges(challenges: List<ChallengeEntity>)

    @Query("UPDATE challenges SET isCompleted = 1, completedAt = :completedAt WHERE id = :id")
    suspend fun markCompleted(id: String, completedAt: Long)

    @Query("SELECT COUNT(*) FROM challenges WHERE isCompleted = 1")
    suspend fun getTotalCompletedCount(): Int

    @Query("SELECT * FROM challenges WHERE id = :id")
    suspend fun getChallenge(id: String): ChallengeEntity?

    @Query("DELETE FROM challenges WHERE assignedDate < :cutoffDate")
    suspend fun deleteOldChallenges(cutoffDate: Long)
}
