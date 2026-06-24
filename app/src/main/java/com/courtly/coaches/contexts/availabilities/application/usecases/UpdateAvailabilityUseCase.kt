package com.courtly.coaches.contexts.availabilities.application.usecases

import com.courtly.coaches.contexts.availabilities.domain.model.Availability
import com.courtly.coaches.contexts.availabilities.domain.model.UpdateAvailabilityParams
import com.courtly.coaches.contexts.availabilities.domain.repository.AvailabilityRepository

class UpdateAvailabilityUseCase(
    private val repository: AvailabilityRepository
) {
    suspend operator fun invoke(
        id: Long,
        params: UpdateAvailabilityParams
    ): Availability {
        return repository.updateAvailability(id, params)
    }
}
