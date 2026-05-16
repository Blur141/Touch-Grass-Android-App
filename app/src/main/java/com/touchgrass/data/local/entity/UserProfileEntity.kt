package com.touchgrass.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.touchgrass.domain.model.UserProfile

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val xp: Int,
    val level: Int,
    val streak: Int,
    val longestStreak: Int,
    val totalChallengesCompleted: Int,
    val totalFocusMinutes: Int,
    val dailyGoalMinutes: Int,
    val joinDate: Long,
    val lastActiveDate: Long,
    val touchGrassScore: Int,
    val petHealthPoints: Int,
    val petLevel: Int,
) {
    fun toDomain() = UserProfile(
        id = id, name = name, xp = xp, level = level, streak = streak,
        longestStreak = longestStreak, totalChallengesCompleted = totalChallengesCompleted,
        totalFocusMinutes = totalFocusMinutes, dailyGoalMinutes = dailyGoalMinutes,
        joinDate = joinDate, lastActiveDate = lastActiveDate, touchGrassScore = touchGrassScore,
        petHealthPoints = petHealthPoints, petLevel = petLevel,
    )

    companion object {
        fun fromDomain(profile: UserProfile) = UserProfileEntity(
            id = profile.id, name = profile.name, xp = profile.xp, level = profile.level,
            streak = profile.streak, longestStreak = profile.longestStreak,
            totalChallengesCompleted = profile.totalChallengesCompleted,
            totalFocusMinutes = profile.totalFocusMinutes, dailyGoalMinutes = profile.dailyGoalMinutes,
            joinDate = profile.joinDate, lastActiveDate = profile.lastActiveDate,
            touchGrassScore = profile.touchGrassScore, petHealthPoints = profile.petHealthPoints,
            petLevel = profile.petLevel,
        )
    }
}
