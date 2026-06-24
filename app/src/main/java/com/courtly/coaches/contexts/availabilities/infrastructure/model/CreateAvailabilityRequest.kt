package com.courtly.coaches.contexts.availabilities.infrastructure.model

import com.courtly.coaches.contexts.availabilities.domain.model.CreateAvailabilityParams

data class CreateAvailabilityRequest(
    val date: String,
    val startTime: String,
    val endTime: String,
    val status: String,
    val coachId: Long
) {
    companion object {
        fun fromDomain(params: CreateAvailabilityParams): CreateAvailabilityRequest {
            return CreateAvailabilityRequest(
                date = params.date,
                startTime = params.startTime,
                endTime = params.endTime,
                status = params.status.name,
                coachId = params.coachId
            )
        }
    }
}
