package com.touchgrass.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.touchgrass.domain.model.FocusSession
import com.touchgrass.domain.model.FocusSessionType

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startTime: Long,
    val endTime: Long?,
    val durationMinutes: Int,
    val sessionType: String,
    val isCompleted: Boolean,
    val xpEarned: Int,
) {
    fun toDomain() = FocusSession(
        id = id, startTime = startTime, endTime = endTime, durationMinutes = durationMinutes,
        sessionType = runCatching { FocusSessionType.valueOf(sessionType) }.getOrDefault(FocusSessionType.CUSTOM),
        isCompleted = isCompleted, xpEarned = xpEarned,
    )

    companion object {
        fun fromDomain(s: FocusSession) = FocusSessionEntity(
            id = s.id, startTime = s.startTime, endTime = s.endTime,
            durationMinutes = s.durationMinutes, sessionType = s.sessionType.name,
            isCompleted = s.isCompleted, xpEarned = s.xpEarned,
        )
    }
}
