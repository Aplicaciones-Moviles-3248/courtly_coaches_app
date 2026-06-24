package com.courtly.coaches.contexts.availabilities.application.usecases

import com.courtly.coaches.contexts.availabilities.domain.repository.AvailabilityRepository

class DeleteAvailabilityUseCase(
    private val repository: AvailabilityRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteAvailability(id)
    }
}
