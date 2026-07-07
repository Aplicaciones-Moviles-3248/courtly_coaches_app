package com.courtly.coaches.contexts.trainingsessions.application.usecases

import com.courtly.coaches.contexts.trainingsessions.domain.model.TrainingSession
import com.courtly.coaches.contexts.trainingsessions.domain.repository.TrainingSessionRepository

class GetMyTrainingSessionsUseCase(
    private val repository: TrainingSessionRepository
) {
    suspend fun execute(): List<TrainingSession> {
        return repository.getMyTrainingSessions()
    }
}
