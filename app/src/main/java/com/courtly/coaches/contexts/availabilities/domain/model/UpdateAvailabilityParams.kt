package com.courtly.coaches.contexts.availabilities.domain.model

data class UpdateAvailabilityParams(
    val date: String,
    val startTime: String,
    val endTime: String,
    val status: AvailabilityStatus
)
