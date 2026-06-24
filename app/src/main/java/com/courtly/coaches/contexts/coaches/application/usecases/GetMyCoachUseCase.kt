package com.courtly.coaches.contexts.coaches.application.usecases

import com.courtly.coaches.contexts.coaches.domain.model.Coach
import com.courtly.coaches.contexts.coaches.domain.repository.CoachRepository

class GetMyCoachUseCase(
    private val repository: CoachRepository
) {
    suspend operator fun invoke(): Coach {
        return repository.getMyCoach()
    }
}