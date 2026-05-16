package com.touchgrass.domain.model

data class UserProfile(
    val id: Int = 1,
    val name: String = "Grasshopper",
    val xp: Int = 0,
    val level: Int = 1,
    val streak: Int = 0,
    val longestStreak: Int = 0,
    val totalChallengesCompleted: Int = 0,
    val totalFocusMinutes: Int = 0,
    val dailyGoalMinutes: Int = 120,
    val joinDate: Long = System.currentTimeMillis(),
    val lastActiveDate: Long = System.currentTimeMillis(),
    val touchGrassScore: Int = 0,
    val petHealthPoints: Int = 100,
    val petLevel: Int = 1,
) {
    val levelTitle: String get() = "Level $level · ${levelName(level)}"

    val xpForNextLevel: Int get() = level * 500

    val xpProgress: Float get() = (xp % (level * 500)).toFloat() / (level * 500).toFloat()

    companion object {
        private val LEVEL_NAMES = listOf(
            "Grass Rookie",          // 1
            "Phone Putter-Downer",   // 2
            "Outdoor Curious",       // 3
            "Touch Grass Apprentice",// 4
            "Offline Warrior",       // 5
            "Nature Curious",        // 6
            "Screen Slayer",         // 7
            "Digital Detoxer",       // 8
            "Nature Survivor",       // 9
            "Algorithm Avoider",     // 10
            "Dopamine Deprogrammer", // 11
            "Notification Ignorer",  // 12
            "Social Media Escapist", // 13
            "Offline Veteran",       // 14
            "Zen Apprentice",        // 15
            "Touch Grass Expert",    // 16
            "Reality Enjoyer",       // 17
            "Screen Time Slayer",    // 18
            "Digital Hermit",        // 19
            "Offline Champion",      // 20
            "Nature Whisperer",      // 21
            "Grass Connoisseur",     // 22
            "Attention Span Guardian",// 23
            "Wi-Fi Avoider",         // 24
            "Cope-Free Human",       // 25
            "Detox Disciple",        // 26
            "Mindful Lurker",        // 27
            "Phone-Free Legend",     // 28
            "Present Moment Haver",  // 29
            "Offline Monk",          // 30
            "Certified Grass Toucher",// 31
            "Sky Appreciator",       // 32
            "Sunlight Collector",    // 33
            "Anti-Doomscroller",     // 34
            "Attention Sovereign",   // 35
            "Skill Issue Resolver",  // 36
            "Blue Light Escapee",    // 37
            "IRL Enthusiast",        // 38
            "Presence Maximalist",   // 39
            "Offline Philosopher",   // 40
            "Reality Veteran",       // 41
            "Grass Grandmaster",     // 42
            "Unplugged Pioneer",     // 43
            "Attention Architect",   // 44
            "Screen-Free Sage",      // 45
            "Digital Ascetic",       // 46
            "Nature\'s Chosen One",  // 47
            "Phone-Free Deity",      // 48
            "Offline Oracle",        // 49
            "Touch Grass Master",    // 50
        )

        private val LEGENDARY_NAMES = listOf(
            "Transcendent Being",
            "Digital Ghost",
            "Grass Demigod",
            "Offline Legend",
            "Reality Ascendant",
            "Screen-Free Supreme",
            "Enlightened Unpluggist",
            "Touch Grass Immortal",
            "Nature\'s Champion",
            "Ultimate Detoxer",
        )

        fun levelName(level: Int): String {
            if (level <= LEVEL_NAMES.size) return LEVEL_NAMES[level - 1]
            return LEGENDARY_NAMES[(level - LEVEL_NAMES.size - 1) % LEGENDARY_NAMES.size]
        }
    }
}
