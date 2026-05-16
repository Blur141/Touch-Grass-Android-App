package com.touchgrass.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.touchgrass.domain.model.Challenge
import com.touchgrass.domain.model.ChallengeCategory
import com.touchgrass.domain.model.ChallengeDifficulty

@Entity(tableName = "challenges")
data class ChallengeEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val xpReward: Int,
    val difficulty: String,
    val category: String,
    val durationMinutes: Int,
    val isCompleted: Boolean,
    val completedAt: Long?,
    val assignedDate: Long,
    val funFact: String,
) {
    fun toDomain() = Challenge(
        id = id, title = title, description = description, emoji = emoji, xpReward = xpReward,
        difficulty = runCatching { ChallengeDifficulty.valueOf(difficulty) }.getOrDefault(ChallengeDifficulty.EASY),
        category = runCatching { ChallengeCategory.valueOf(category) }.getOrDefault(ChallengeCategory.OUTDOOR),
        durationMinutes = durationMinutes, isCompleted = isCompleted,
        completedAt = completedAt, assignedDate = assignedDate, funFact = funFact,
    )

    companion object {
        fun fromDomain(c: Challenge) = ChallengeEntity(
            id = c.id, title = c.title, description = c.description, emoji = c.emoji,
            xpReward = c.xpReward, difficulty = c.difficulty.name, category = c.category.name,
            durationMinutes = c.durationMinutes, isCompleted = c.isCompleted,
            completedAt = c.completedAt, assignedDate = c.assignedDate, funFact = c.funFact,
        )
    }
}
