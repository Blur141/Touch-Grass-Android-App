package com.touchgrass.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.touchgrass.domain.model.Achievement
import com.touchgrass.domain.model.AchievementType

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val xpReward: Int,
    val isUnlocked: Boolean,
    val unlockedAt: Long?,
    val type: String,
    val requirement: Int,
    val currentProgress: Int,
) {
    fun toDomain() = Achievement(
        id = id, title = title, description = description, emoji = emoji, xpReward = xpReward,
        isUnlocked = isUnlocked, unlockedAt = unlockedAt,
        type = runCatching { AchievementType.valueOf(type) }.getOrDefault(AchievementType.SPECIAL),
        requirement = requirement, currentProgress = currentProgress,
    )

    companion object {
        fun fromDomain(a: Achievement) = AchievementEntity(
            id = a.id, title = a.title, description = a.description, emoji = a.emoji,
            xpReward = a.xpReward, isUnlocked = a.isUnlocked, unlockedAt = a.unlockedAt,
            type = a.type.name, requirement = a.requirement, currentProgress = a.currentProgress,
        )
    }
}
