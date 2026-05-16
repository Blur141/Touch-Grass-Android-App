package com.touchgrass.data.local.dao

import androidx.room.*
import com.touchgrass.data.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun observeProfile(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getProfile(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET xp = xp + :amount WHERE id = 1")
    suspend fun addXp(amount: Int)

    @Query("UPDATE user_profile SET streak = :streak, longestStreak = MAX(longestStreak, :streak) WHERE id = 1")
    suspend fun updateStreak(streak: Int)

    @Query("UPDATE user_profile SET totalChallengesCompleted = totalChallengesCompleted + 1, xp = xp + :xp WHERE id = 1")
    suspend fun incrementChallengesAndXp(xp: Int)

    @Query("UPDATE user_profile SET petHealthPoints = :health, petLevel = :level WHERE id = 1")
    suspend fun updatePetStatus(health: Int, level: Int)

    @Query("UPDATE user_profile SET touchGrassScore = :score WHERE id = 1")
    suspend fun updateTouchGrassScore(score: Int)

    @Query("UPDATE user_profile SET name = :name WHERE id = 1")
    suspend fun updateName(name: String)

    @Query("UPDATE user_profile SET totalFocusMinutes = totalFocusMinutes + :minutes WHERE id = 1")
    suspend fun addFocusMinutes(minutes: Int)

    @Query("UPDATE user_profile SET lastActiveDate = :timestamp WHERE id = 1")
    suspend fun updateLastActiveDate(timestamp: Long)
}
