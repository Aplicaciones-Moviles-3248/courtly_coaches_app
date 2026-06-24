package com.courtly.coaches.contexts.availabilities.application.usecases

import com.courtly.coaches.contexts.availabilities.domain.model.Availability
import com.courtly.coaches.contexts.availabilities.domain.model.CreateAvailabilityParams
import com.courtly.coaches.contexts.availabilities.domain.repository.AvailabilityRepository

class CreateAvailabilityUseCase(
    private val repository: AvailabilityRepository
) {
    suspend operator fun invoke(params: CreateAvailabilityParams): Availability {
        return repository.createAvailability(params)
    }
}
