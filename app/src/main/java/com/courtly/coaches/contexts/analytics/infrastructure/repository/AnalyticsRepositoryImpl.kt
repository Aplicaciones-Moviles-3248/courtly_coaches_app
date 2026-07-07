package com.courtly.coaches.contexts.analytics.infrastructure.repository

import com.courtly.coaches.contexts.analytics.domain.model.Metric
import com.courtly.coaches.contexts.analytics.domain.repository.AnalyticsRepository
import com.courtly.coaches.contexts.analytics.infrastructure.remote.AnalyticsApiService

class AnalyticsRepositoryImpl (
    private val apiService: AnalyticsApiService
) : AnalyticsRepository {

    override suspend fun getMyMetrics(): List<Metric> {
        return apiService
            .getMyMetrics()
            .map { it.toDomain() }
    }

    override suspend fun getMetricById(id: Long): Metric {
        return apiService
            .getMetricById(id)
            .toDomain()
    }
}