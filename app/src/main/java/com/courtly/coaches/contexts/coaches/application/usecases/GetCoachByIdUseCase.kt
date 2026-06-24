package com.courtly.coaches.contexts.coaches.application.usecases

import com.courtly.coaches.contexts.coaches.domain.model.Coach
import com.courtly.coaches.contexts.coaches.domain.repository.CoachRepository

class GetCoachByIdUseCase(
    private val repository: CoachRepository
) {
    suspend operator fun invoke(id: Long): Coach {
        return repository.getCoachById(id)
    }
}