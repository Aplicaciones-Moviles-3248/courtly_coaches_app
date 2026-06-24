package com.courtly.coaches.contexts.coaches.application.usecases

import com.courtly.coaches.contexts.coaches.domain.repository.CoachRepository

class DeleteCoachUseCase(
    private val repository: CoachRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteCoach(id)
    }
}