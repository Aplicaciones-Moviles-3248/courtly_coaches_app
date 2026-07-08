package com.courtly.coaches.contexts.analytics.domain.model

data class Metric(
    val id: Long,
    val metricType: MetricType,
    val value: Double,
    val period: String,
    val createdAt: String?,
    val coach: CoachSummary?
)

data class CoachSummary(
    val id: Long,
    val name: String
)