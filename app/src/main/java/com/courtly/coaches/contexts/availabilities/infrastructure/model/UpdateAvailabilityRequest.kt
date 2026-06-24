package com.courtly.coaches.contexts.availabilities.infrastructure.model

import com.courtly.coaches.contexts.availabilities.domain.model.UpdateAvailabilityParams

data class UpdateAvailabilityRequest(
    val date: String,
    val startTime: String,
    val endTime: String,
    val status: String
) {
    companion object {
        fun fromDomain(params: UpdateAvailabilityParams): UpdateAvailabilityRequest {
            return UpdateAvailabilityRequest(
                date = params.date,
                startTime = params.startTime,
                endTime = params.endTime,
                status = params.status.name
            )
        }
    }
}
