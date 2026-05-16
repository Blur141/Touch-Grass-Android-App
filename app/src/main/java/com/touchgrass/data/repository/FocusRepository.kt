package com.touchgrass.data.repository

import com.touchgrass.data.local.dao.FocusSessionDao
import com.touchgrass.data.local.entity.FocusSessionEntity
import com.touchgrass.domain.model.FocusSession
import com.touchgrass.domain.model.FocusSessionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FocusRepository @Inject constructor(
    private val focusSessionDao: FocusSessionDao,
) {
    fun observeRecentSessions(): Flow<List<FocusSession>> =
        focusSessionDao.observeRecentSessions().map { it.map { e -> e.toDomain() } }

    suspend fun startSession(type: FocusSessionType, durationMinutes: Int): Long {
        val entity = FocusSessionEntity(
            startTime = System.currentTimeMillis(), endTime = null,
            durationMinutes = durationMinutes, sessionType = type.name,
            isCompleted = false, xpEarned = 0,
        )
        return focusSessionDao.insertSession(entity)
    }

    suspend fun completeSession(id: Long, durationMinutes: Int) {
        val xpEarned = durationMinutes * 2
        focusSessionDao.completeSession(id, System.currentTimeMillis(), xpEarned)
    }

    suspend fun getTotalFocusMinutes(): Int = focusSessionDao.getTotalFocusMinutes() ?: 0

    suspend fun getCompletedSessionCount(): Int = focusSessionDao.getCompletedSessionCount()
}
