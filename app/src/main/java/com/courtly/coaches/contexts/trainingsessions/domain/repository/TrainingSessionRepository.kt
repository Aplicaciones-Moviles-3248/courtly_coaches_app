package com.courtly.coaches.contexts.trainingsessions.domain.repository

import com.courtly.coaches.contexts.trainingsessions.domain.model.TrainingSession

interface TrainingSessionRepository {
    suspend fun getMyTrainingSessions(): List<TrainingSession>
    suspend fun acceptTrainingSession(id: Long): TrainingSession
    suspend fun rejectTrainingSession(id: Long, reason: String? = null): TrainingSession
}
