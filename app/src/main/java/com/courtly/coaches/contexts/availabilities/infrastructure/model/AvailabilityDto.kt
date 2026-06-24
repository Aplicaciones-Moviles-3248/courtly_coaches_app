package com.courtly.coaches.contexts.availabilities.infrastructure.model

import com.courtly.coaches.contexts.availabilities.domain.model.Availability
import com.courtly.coaches.contexts.availabilities.domain.model.AvailabilityCoachSummary
import com.courtly.coaches.contexts.availabilities.domain.model.AvailabilityStatus

data class AvailabilityDto(
    val id: Long,
    val date: String,
    val startTime: String,
    val endTime: String,
    val status: String,
    val createdAt: String? = null,
    val coach: CoachSummaryDto? = null
) {
    data class CoachSummaryDto(
        val id: Long,
        val name: String
    )

    fun toDomain(): Availability {
        return Availability(
            id = id,
            date = date,
            startTime = startTime,
            endTime = endTime,
            status = AvailabilityStatus.fromApiValue(status),
            createdAt = createdAt,
            coach = coach?.let {
                AvailabilityCoachSummary(
                    id = it.id,
                    name = it.name
                )
            }
        )
    }
}
