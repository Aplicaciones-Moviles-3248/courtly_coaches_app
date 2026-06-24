package com.courtly.coaches.contexts.coaches.application.usecases

import com.courtly.coaches.contexts.coaches.domain.model.Coach
import com.courtly.coaches.contexts.coaches.domain.model.CreateCoachParams
import com.courtly.coaches.contexts.coaches.domain.repository.CoachRepository

class CreateCoachUseCase(
    private val repository: CoachRepository
) {
    suspend operator fun invoke(
        params: CreateCoachParams
    ): Coach {
        return repository.createCoach(params)
    }
}