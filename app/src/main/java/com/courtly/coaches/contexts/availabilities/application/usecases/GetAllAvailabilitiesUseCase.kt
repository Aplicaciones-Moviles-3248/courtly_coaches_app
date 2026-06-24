package com.courtly.coaches.contexts.availabilities.application.usecases

import com.courtly.coaches.contexts.availabilities.domain.model.Availability
import com.courtly.coaches.contexts.availabilities.domain.repository.AvailabilityRepository

class GetAllAvailabilitiesUseCase(
    private val repository: AvailabilityRepository
) {
    suspend operator fun invoke(): List<Availability> {
        return repository.getAllAvailabilities()
    }
}
