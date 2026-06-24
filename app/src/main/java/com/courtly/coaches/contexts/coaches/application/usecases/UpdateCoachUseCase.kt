package com.courtly.coaches.contexts.coaches.application.usecases

import com.courtly.coaches.contexts.coaches.domain.model.Coach
import com.courtly.coaches.contexts.coaches.domain.model.UpdateCoachParams
import com.courtly.coaches.contexts.coaches.domain.repository.CoachRepository

class UpdateCoachUseCase(
    private val repository: CoachRepository
) {
    suspend operator fun invoke(
        id: Long,
        params: UpdateCoachParams
    ): Coach {
        return repository.updateCoach(id, params)
    }
}