package com.courtly.coaches.contexts.trainingsessions.infrastructure.repository

import com.courtly.coaches.contexts.trainingsessions.domain.model.TrainingSession
import com.courtly.coaches.contexts.trainingsessions.domain.repository.TrainingSessionRepository
import com.courtly.coaches.contexts.trainingsessions.infrastructure.model.RejectSessionRequest
import com.courtly.coaches.contexts.trainingsessions.infrastructure.remote.TrainingSessionApiService

class TrainingSessionRepositoryImpl(
    private val apiService: TrainingSessionApiService
) : TrainingSessionRepository {

    override suspend fun getMyTrainingSessions(): List<TrainingSession> {
        return apiService.getMyTrainingSessions().map { it.toDomain() }
    }

    override suspend fun acceptTrainingSession(id: Long): TrainingSession {
        return apiService.acceptTrainingSession(id).toDomain()
    }

    override suspend fun rejectTrainingSession(id: Long, reason: String?): TrainingSession {
        val request = reason?.let { RejectSessionRequest(it) }
        return apiService.rejectTrainingSession(id, request).toDomain()
    }
}
