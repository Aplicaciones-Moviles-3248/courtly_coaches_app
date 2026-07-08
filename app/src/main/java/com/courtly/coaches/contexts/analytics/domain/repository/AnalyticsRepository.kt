package com.courtly.coaches.contexts.analytics.domain.repository

import com.courtly.coaches.contexts.analytics.domain.model.Metric

interface AnalyticsRepository {

    suspend fun getMyMetrics(): List<Metric>

    suspend fun getMetricById(id: Long): Metric
}