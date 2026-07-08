package com.courtly.coaches.contexts.trainingsessions.application.usecases

import com.courtly.coaches.contexts.trainingsessions.domain.model.TrainingSession
import com.courtly.coaches.contexts.trainingsessions.domain.repository.TrainingSessionRepository

class RejectTrainingSessionUseCase(
    private val repository: TrainingSessionRepository
) {
    suspend fun execute(id: Long, reason: String? = null): TrainingSession {
        return repository.rejectTrainingSession(id, reason)
    }
}
