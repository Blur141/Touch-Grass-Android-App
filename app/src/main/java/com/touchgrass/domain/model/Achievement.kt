package com.touchgrass.domain.model

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val xpReward: Int,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null,
    val type: AchievementType,
    val requirement: Int,
    val currentProgress: Int = 0,
) {
    val progress: Float get() = (currentProgress.toFloat() / requirement).coerceAtMost(1f)
}

enum class AchievementType {
    STREAK, CHALLENGES, FOCUS_TIME, SCREEN_TIME_GOAL, LEVEL, SPECIAL;
}

val ACHIEVEMENT_DEFINITIONS = listOf(
    // Challenges
    Achievement("a1", "First Blade of Grass", "Complete your first challenge", "🌱", 100, type = AchievementType.CHALLENGES, requirement = 1),
    Achievement("a2", "Getting Grounded", "Complete 10 challenges", "🌿", 200, type = AchievementType.CHALLENGES, requirement = 10),
    Achievement("a3", "Nature Enthusiast", "Complete 50 challenges", "🌳", 500, type = AchievementType.CHALLENGES, requirement = 50),
    Achievement("a19", "Challenge Completionist", "Complete 100 challenges", "💯", 2000, type = AchievementType.CHALLENGES, requirement = 100),
    Achievement("a26", "Challenge Addict", "Complete 250 challenges", "🏅", 5000, type = AchievementType.CHALLENGES, requirement = 250),

    // Streaks
    Achievement("a4", "Day 1", "Maintain a 1-day streak", "🔥", 50, type = AchievementType.STREAK, requirement = 1),
    Achievement("a5", "Week Warrior", "Maintain a 7-day streak", "🔥🔥", 300, type = AchievementType.STREAK, requirement = 7),
    Achievement("a6", "Month of Grass", "Maintain a 30-day streak", "🏆", 1000, type = AchievementType.STREAK, requirement = 30),
    Achievement("a24", "Century Run", "Maintain a 100-day streak", "💎", 5000, type = AchievementType.STREAK, requirement = 100),
    Achievement("a20", "Year-Long Warrior", "Maintain a 365-day streak", "🌟", 10000, type = AchievementType.STREAK, requirement = 365),

    // Focus
    Achievement("a7", "First Focus", "Complete your first focus session", "🎯", 100, type = AchievementType.FOCUS_TIME, requirement = 1),
    Achievement("a8", "Deep Work", "Accumulate 10 hours of focus time", "⚡", 500, type = AchievementType.FOCUS_TIME, requirement = 600),
    Achievement("a16", "Focus Beast", "Complete 10 focus sessions (250+ min)", "🧠", 500, type = AchievementType.FOCUS_TIME, requirement = 10),
    Achievement("a21", "Flow State Master", "Accumulate 50 hours of focus time", "🧘", 2000, type = AchievementType.FOCUS_TIME, requirement = 3000),

    // Screen Time Goal
    Achievement("a9", "Goal Getter", "Meet your daily screen time goal", "✅", 100, type = AchievementType.SCREEN_TIME_GOAL, requirement = 1),
    Achievement("a10", "Screen Slayer", "Meet your daily goal 7 days in a row", "⚔️", 500, type = AchievementType.SCREEN_TIME_GOAL, requirement = 7),
    Achievement("a27", "Digital Discipline", "Meet your daily goal 30 days in a row", "🛡️", 2000, type = AchievementType.SCREEN_TIME_GOAL, requirement = 30),

    // Levels
    Achievement("a11", "Grass Rookie", "Reach Level 1", "🌱", 0, type = AchievementType.LEVEL, requirement = 1),
    Achievement("a12", "Offline Warrior", "Reach Level 5", "⚔️", 500, type = AchievementType.LEVEL, requirement = 5),
    Achievement("a13", "Social Media Escapist", "Reach Level 10", "🚀", 2000, type = AchievementType.LEVEL, requirement = 10),
    Achievement("a28", "Touch Grass Expert", "Reach Level 20", "🎓", 5000, type = AchievementType.LEVEL, requirement = 20),
    Achievement("a29", "Offline Legend", "Reach Level 50", "👑", 20000, type = AchievementType.LEVEL, requirement = 50),

    // Special
    Achievement("a14", "Phoneless Pioneer", "Spend 2+ hours offline in a day", "🌟", 300, type = AchievementType.SPECIAL, requirement = 1),
    Achievement("a15", "Dawn Patrol", "Open the app before 7 AM", "🌄", 150, type = AchievementType.SPECIAL, requirement = 1),
    Achievement("a17", "Sleep Scheduler", "Enable the sleep schedule", "😴", 100, type = AchievementType.SPECIAL, requirement = 1),
    Achievement("a18", "Block Party", "Block 5 or more apps", "🔒", 200, type = AchievementType.SPECIAL, requirement = 5),
    Achievement("a22", "Speed Demon", "Complete all daily challenges in one session", "⚡", 300, type = AchievementType.SPECIAL, requirement = 1),
    Achievement("a23", "Anti-Doomscroller", "Block an app you would normally use for 1 hour", "📵", 250, type = AchievementType.SPECIAL, requirement = 1),
    Achievement("a25", "XP Millionaire", "Earn 10,000 total XP", "⭐", 1000, type = AchievementType.SPECIAL, requirement = 10000),
    Achievement("a30", "Hardcore Mode", "Set your daily screen time goal to 1 hour or less", "💀", 500, type = AchievementType.SPECIAL, requirement = 1),
)
