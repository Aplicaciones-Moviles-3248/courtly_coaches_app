package com.courtly.coaches.contexts.trainingsessions.application.usecases

import com.courtly.coaches.contexts.trainingsessions.domain.model.TrainingSession
import com.courtly.coaches.contexts.trainingsessions.domain.repository.TrainingSessionRepository

class AcceptTrainingSessionUseCase(
    private val repository: TrainingSessionRepository
) {
    suspend fun execute(id: Long): TrainingSession {
        return repository.acceptTrainingSession(id)
    }
}
