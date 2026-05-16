package com.touchgrass.data.repository

import com.touchgrass.data.local.dao.AchievementDao
import com.touchgrass.data.local.dao.UserProfileDao
import com.touchgrass.data.local.entity.AchievementEntity
import com.touchgrass.data.local.entity.UserProfileEntity
import com.touchgrass.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GamificationRepository @Inject constructor(
    private val userProfileDao: UserProfileDao,
    private val achievementDao: AchievementDao,
) {
    fun observeProfile(): Flow<UserProfile> = userProfileDao.observeProfile().map { entity ->
        entity?.toDomain() ?: createDefaultProfile()
    }

    fun observeAchievements(): Flow<List<Achievement>> =
        achievementDao.observeAllAchievements().map { entities ->
            if (entities.isEmpty()) ACHIEVEMENT_DEFINITIONS
            else entities.map { it.toDomain() }
        }

    suspend fun ensureProfileExists() {
        if (userProfileDao.getProfile() == null) {
            userProfileDao.upsertProfile(UserProfileEntity.fromDomain(createDefaultProfile()))
        }
    }

    suspend fun ensureAchievementsSeeded() {
        val existing = achievementDao.observeAllAchievements().first()
        val existingIds = existing.map { it.id }.toSet()
        val toAdd = ACHIEVEMENT_DEFINITIONS.filter { it.id !in existingIds }
        if (toAdd.isNotEmpty()) {
            achievementDao.upsertAchievements(toAdd.map { AchievementEntity.fromDomain(it) })
        }
    }

    suspend fun awardXp(amount: Int) {
        userProfileDao.addXp(amount)
        checkLevelUp()
    }

    suspend fun completeChallengeXp(xp: Int) {
        userProfileDao.incrementChallengesAndXp(xp)
        userProfileDao.updateLastActiveDate(System.currentTimeMillis())
        checkLevelUp()
        checkChallengeAchievements()
    }

    suspend fun awardFocusSession(minutes: Int) {
        userProfileDao.addFocusMinutes(minutes)
        userProfileDao.addXp(minutes * 2)
        checkLevelUp()
        checkFocusAchievements()
    }

    suspend fun updateStreak(newStreak: Int) {
        userProfileDao.updateStreak(newStreak)
        achievementDao.updateProgress("a4", newStreak)
        achievementDao.updateProgress("a5", newStreak)
        achievementDao.updateProgress("a6", newStreak)
        achievementDao.updateProgress("a24", newStreak)
        achievementDao.updateProgress("a20", newStreak)

        if (newStreak >= 1) unlockAchievementIfEligible("a4", 1)
        if (newStreak >= 7) unlockAchievementIfEligible("a5", 7)
        if (newStreak >= 30) unlockAchievementIfEligible("a6", 30)
        if (newStreak >= 100) unlockAchievementIfEligible("a24", 100)
        if (newStreak >= 365) unlockAchievementIfEligible("a20", 365)
    }

    suspend fun updatePetFromScreenTime(screenTimeMs: Long, dailyGoalMs: Long) {
        val profile = userProfileDao.getProfile() ?: return
        val overageRatio = (screenTimeMs.toFloat() / dailyGoalMs).coerceAtMost(3f)
        val healthDelta = when {
            overageRatio > 2f -> -20
            overageRatio > 1.5f -> -10
            overageRatio > 1f -> -5
            overageRatio < 0.5f -> +15
            overageRatio < 0.8f -> +5
            else -> 0
        }
        val newHealth = (profile.petHealthPoints + healthDelta).coerceIn(0, 100)
        val newPetLevel = when {
            profile.xp >= 5000 -> 5
            profile.xp >= 2000 -> 4
            profile.xp >= 1000 -> 3
            profile.xp >= 300 -> 2
            else -> 1
        }
        userProfileDao.updatePetStatus(newHealth, newPetLevel)
    }

    private suspend fun checkLevelUp() {
        val profile = userProfileDao.getProfile() ?: return
        val newLevel = calculateLevel(profile.xp)
        if (newLevel > profile.level) {
            userProfileDao.upsertProfile(profile.copy(level = newLevel))
            checkLevelAchievements(newLevel)
        }
    }

    private suspend fun checkChallengeAchievements() {
        val profile = userProfileDao.getProfile() ?: return
        val completed = profile.totalChallengesCompleted
        achievementDao.updateProgress("a1", completed)
        achievementDao.updateProgress("a2", completed)
        achievementDao.updateProgress("a3", completed)
        achievementDao.updateProgress("a19", completed)

        if (completed >= 1) unlockAchievementIfEligible("a1", 1)
        if (completed >= 10) unlockAchievementIfEligible("a2", 10)
        if (completed >= 50) unlockAchievementIfEligible("a3", 50)
        if (completed >= 100) unlockAchievementIfEligible("a19", 100)
    }

    private suspend fun checkFocusAchievements() {
        val profile = userProfileDao.getProfile() ?: return
        val mins = profile.totalFocusMinutes
        achievementDao.updateProgress("a7", if (mins > 0) 1 else 0)
        achievementDao.updateProgress("a8", mins)
        achievementDao.updateProgress("a16", (mins / 25).coerceAtLeast(0))

        if (mins >= 1) unlockAchievementIfEligible("a7", 1)
        if (mins >= 600) unlockAchievementIfEligible("a8", 600)
        if (mins >= 250) unlockAchievementIfEligible("a16", 10)
    }

    private suspend fun checkLevelAchievements(level: Int) {
        achievementDao.updateProgress("a11", level)
        achievementDao.updateProgress("a12", level)
        achievementDao.updateProgress("a13", level)

        if (level >= 1) unlockAchievementIfEligible("a11", 1)
        if (level >= 5) unlockAchievementIfEligible("a12", 5)
        if (level >= 10) unlockAchievementIfEligible("a13", 10)
    }

    private suspend fun unlockAchievementIfEligible(id: String, requirement: Int) {
        val achievement = achievementDao.getAchievement(id) ?: return
        if (!achievement.isUnlocked && achievement.currentProgress >= requirement) {
            achievementDao.unlockAchievement(id, System.currentTimeMillis())
            userProfileDao.addXp(achievement.xpReward)
            checkLevelUp()
        }
    }

    private fun calculateLevel(xp: Int): Int {
        var level = 1
        var xpRequired = 500
        var remaining = xp
        while (remaining >= xpRequired) {
            remaining -= xpRequired
            level++
            xpRequired = level * 500
        }
        return level
    }

    suspend fun updateUserName(name: String) {
        userProfileDao.updateName(name.trim().ifBlank { "Grasshopper" })
    }

    suspend fun getLastActiveDate(): Long =
        userProfileDao.getProfile()?.lastActiveDate ?: 0L

    private fun createDefaultProfile() = UserProfile(
        name = "Grasshopper",
        joinDate = System.currentTimeMillis(),
        lastActiveDate = System.currentTimeMillis(),
    )
}
