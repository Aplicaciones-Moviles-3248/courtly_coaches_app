package com.courtly.coaches.contexts.availabilities.domain.model

data class CreateAvailabilityParams(
    val date: String,
    val startTime: String,
    val endTime: String,
    val status: AvailabilityStatus,
    val coachId: Long
)
