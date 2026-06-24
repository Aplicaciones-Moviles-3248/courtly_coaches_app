package com.courtly.coaches.contexts.availabilities.domain.model

data class Availability(
    val id: Long,
    val date: String,
    val startTime: String,
    val endTime: String,
    val status: AvailabilityStatus,
    val createdAt: String? = null,
    val coach: AvailabilityCoachSummary? = null
)
