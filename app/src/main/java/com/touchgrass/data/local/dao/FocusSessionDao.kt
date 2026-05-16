package com.touchgrass.data.local.dao

import androidx.room.*
import com.touchgrass.data.local.entity.FocusSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusSessionDao {
    @Query("SELECT * FROM focus_sessions ORDER BY startTime DESC LIMIT :limit")
    fun observeRecentSessions(limit: Int = 20): Flow<List<FocusSessionEntity>>

    @Insert
    suspend fun insertSession(session: FocusSessionEntity): Long

    @Update
    suspend fun updateSession(session: FocusSessionEntity)

    @Query("UPDATE focus_sessions SET isCompleted = 1, endTime = :endTime, xpEarned = :xp WHERE id = :id")
    suspend fun completeSession(id: Long, endTime: Long, xp: Int)

    @Query("SELECT SUM(durationMinutes) FROM focus_sessions WHERE isCompleted = 1")
    suspend fun getTotalFocusMinutes(): Int?

    @Query("SELECT COUNT(*) FROM focus_sessions WHERE isCompleted = 1")
    suspend fun getCompletedSessionCount(): Int

    @Query("SELECT * FROM focus_sessions WHERE startTime >= :startTime AND isCompleted = 1 ORDER BY startTime DESC")
    fun observeSessionsSince(startTime: Long): Flow<List<FocusSessionEntity>>
}
