package com.courtly.coaches.contexts.analytics.infrastructure.model

import com.courtly.coaches.contexts.analytics.domain.model.CoachSummary
import com.courtly.coaches.contexts.analytics.domain.model.Metric
import com.courtly.coaches.contexts.analytics.domain.model.MetricType

data class MetricDto(
    val id: Long,
    val metricType: String,
    val value: Double,
    val period: String,
    val createdAt: String?,
    val coach: CoachSummaryDto?
) {

    data class CoachSummaryDto(
        val id: Long,
        val name: String
    )

    fun toDomain() =
        Metric(
            id = id,
            metricType = MetricType.valueOf(metricType),
            value = value,
            period = period,
            createdAt = createdAt,
            coach = coach?.let {
                CoachSummary(
                    id = it.id,
                    name = it.name
                )
            }
        )
}