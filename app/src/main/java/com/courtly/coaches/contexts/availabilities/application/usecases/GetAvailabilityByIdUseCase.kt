package com.courtly.coaches.contexts.availabilities.application.usecases

import com.courtly.coaches.contexts.availabilities.domain.model.Availability
import com.courtly.coaches.contexts.availabilities.domain.repository.AvailabilityRepository

class GetAvailabilityByIdUseCase(
    private val repository: AvailabilityRepository
) {
    suspend operator fun invoke(id: Long): Availability {
        return repository.getAvailabilityById(id)
    }
}
