package com.touchgrass.domain.model

data class FocusSession(
    val id: Long = 0,
    val startTime: Long,
    val endTime: Long? = null,
    val durationMinutes: Int,
    val sessionType: FocusSessionType,
    val isCompleted: Boolean = false,
    val xpEarned: Int = 0,
)

enum class FocusSessionType(
    val label: String,
    val emoji: String,
    val defaultMinutes: Int,
    val description: String,
) {
    POMODORO("Pomodoro", "🍅", 25, "25 min focus + 5 min break. Classic."),
    DEEP_WORK("Deep Work", "⚡", 90, "90 minutes of pure, distraction-free work."),
    QUICK_SPRINT("Quick Sprint", "🏃", 15, "15 minutes. Just do the thing."),
    POWER_HOUR("Power Hour", "💪", 60, "60 minutes. No excuses."),
    CUSTOM("Custom", "⚙️", 30, "Your rules, your time.");
}

data class FocusState(
    val isActive: Boolean = false,
    val sessionType: FocusSessionType = FocusSessionType.POMODORO,
    val totalSeconds: Int = 1500,
    val remainingSeconds: Int = 1500,
    val isBreakTime: Boolean = false,
    val pomodoroCount: Int = 0,
) {
    val progress: Float get() = 1f - (remainingSeconds.toFloat() / totalSeconds)
    val formattedTime: String get() {
        val minutes = remainingSeconds / 60
        val seconds = remainingSeconds % 60
        return "%02d:%02d".format(minutes, seconds)
    }
}
