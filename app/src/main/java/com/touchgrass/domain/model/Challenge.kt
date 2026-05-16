package com.touchgrass.domain.model

data class Challenge(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val xpReward: Int,
    val difficulty: ChallengeDifficulty,
    val category: ChallengeCategory,
    val durationMinutes: Int = 0,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val assignedDate: Long = System.currentTimeMillis(),
    val funFact: String = "",
)

enum class ChallengeDifficulty(val label: String, val color: Long) {
    EASY("Easy", 0xFF22C55E),
    MEDIUM("Medium", 0xFFF59E0B),
    HARD("Hard", 0xFFEF4444),
    LEGENDARY("Legendary", 0xFF8B5CF6);
}

enum class ChallengeCategory(val label: String, val emoji: String) {
    OUTDOOR("Go Outside", "🌿"),
    PHYSICAL("Move Body", "💪"),
    SOCIAL("Be Human", "🤝"),
    MINDFULNESS("Breathe", "🧘"),
    DIGITAL_DETOX("Skill Issue", "📵"),
    HYDRATION("Stay Hydrated", "💧");
}

val CHALLENGE_POOL = listOf(
    Challenge("c1", "Touch Actual Grass", "Find real grass. Touch it. Feel the Earth.", "🌱", 50, ChallengeDifficulty.EASY, ChallengeCategory.OUTDOOR, 5, funFact = "Grass has been on Earth for 66 million years. Your TikTok feed has not."),
    Challenge("c2", "Walk 10 Minutes Outside", "Put the phone in your pocket. Walk somewhere. Breathe air.", "🚶", 75, ChallengeDifficulty.EASY, ChallengeCategory.OUTDOOR, 10, funFact = "10 minutes of walking reduces anxiety by 30%. Scrolling does not."),
    Challenge("c3", "Watch the Sunset", "Actually watch it. Not record it. Watch it.", "🌅", 100, ChallengeDifficulty.EASY, ChallengeCategory.OUTDOOR, 15, funFact = "Sunsets are free. Your Netflix subscription is not."),
    Challenge("c4", "Drink 3 Glasses of Water", "Hydrate or dydrate. Your choice.", "💧", 40, ChallengeDifficulty.EASY, ChallengeCategory.HYDRATION, 10, funFact = "You are 60% water. You are also spending 6 hours on Instagram."),
    Challenge("c5", "No Phone for 30 Minutes", "The apps will still be there when you get back. Unfortunately.", "📵", 100, ChallengeDifficulty.MEDIUM, ChallengeCategory.DIGITAL_DETOX, 30, funFact = "The average human checks their phone 96 times per day. That's every 10 minutes."),
    Challenge("c6", "Call a Friend (Voice)", "Use your phone as a phone. Wild, right?", "📞", 80, ChallengeDifficulty.EASY, ChallengeCategory.SOCIAL, 10, funFact = "Voice calls make people feel 30% more connected than texts."),
    Challenge("c7", "Read 5 Pages of a Book", "A physical book. Not a Twitter thread.", "📚", 90, ChallengeDifficulty.EASY, ChallengeCategory.MINDFULNESS, 15, funFact = "Reading 20 minutes a day puts you in the top 5% of readers globally."),
    Challenge("c8", "Do 20 Push-Ups", "Not scroll. Push. Up.", "💪", 80, ChallengeDifficulty.MEDIUM, ChallengeCategory.PHYSICAL, 5, funFact = "Your thumbs are getting very strong from scrolling. The rest of you less so."),
    Challenge("c9", "Stretch for 5 Minutes", "Your back has been screaming. Listen to it.", "🧘", 50, ChallengeDifficulty.EASY, ChallengeCategory.PHYSICAL, 5, funFact = "Sitting for 8+ hours is as bad for you as smoking. Just saying."),
    Challenge("c10", "Eat a Meal Without Screens", "Taste the food. It's there.", "🍽️", 70, ChallengeDifficulty.MEDIUM, ChallengeCategory.MINDFULNESS, 20, funFact = "Mindful eating reduces overeating by 20%. You've been eating cold food looking at memes."),
    Challenge("c11", "Write 3 Things You're Grateful For", "With a pen. On paper. Boomer style.", "✍️", 60, ChallengeDifficulty.EASY, ChallengeCategory.MINDFULNESS, 5, funFact = "Gratitude journaling reduces cortisol by 23%. Your doomscrolling does the opposite."),
    Challenge("c12", "Take a Cold Shower", "Shock yourself into consciousness.", "🚿", 120, ChallengeDifficulty.HARD, ChallengeCategory.PHYSICAL, 5, funFact = "Cold showers increase dopamine by 250%. More than any reel ever will."),
    Challenge("c13", "Talk to a Stranger", "In real life. Insane concept.", "👋", 150, ChallengeDifficulty.HARD, ChallengeCategory.SOCIAL, 5, funFact = "Random acts of connection boost happiness more than social media ever could."),
    Challenge("c14", "No Social Media for 2 Hours", "Legendary difficulty. You can do it.", "🏆", 200, ChallengeDifficulty.LEGENDARY, ChallengeCategory.DIGITAL_DETOX, 120, funFact = "The apps are designed by teams of PhDs to keep you addicted. Beating them is legendary."),
    Challenge("c15", "Watch Clouds for 5 Minutes", "Lie on your back. Look up. No recording.", "☁️", 60, ChallengeDifficulty.EASY, ChallengeCategory.OUTDOOR, 5, funFact = "Cloud watching activates the default mode network, boosting creativity."),
    Challenge("c16", "Cook a Meal From Scratch", "Without looking up a recipe mid-cook on Instagram.", "👨‍🍳", 130, ChallengeDifficulty.MEDIUM, ChallengeCategory.MINDFULNESS, 45, funFact = "People who cook at home are 35% healthier than people who UberEats every day."),
    Challenge("c17", "Do a 5-Minute Meditation", "Just sit there. Breathe. That's it.", "🧘‍♀️", 70, ChallengeDifficulty.EASY, ChallengeCategory.MINDFULNESS, 5, funFact = "5 minutes of meditation changes your brain more than 5 minutes of scrolling. Weirdly."),
    Challenge("c18", "Go to Bed Before Midnight", "Revolutionary concept for a reason.", "😴", 100, ChallengeDifficulty.MEDIUM, ChallengeCategory.DIGITAL_DETOX, 0, funFact = "Phone use at night reduces melatonin by 50%. No wonder you're tired."),
)
