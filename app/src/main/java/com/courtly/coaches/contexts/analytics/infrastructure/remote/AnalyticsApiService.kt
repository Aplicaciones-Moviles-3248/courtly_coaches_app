package com.courtly.coaches.contexts.analytics.infrastructure.remote

import com.courtly.coaches.contexts.analytics.infrastructure.model.MetricDto
import retrofit2.http.GET
import retrofit2.http.Path

interface AnalyticsApiService {

    @GET("analytics/me")
    suspend fun getMyMetrics(): List<MetricDto>

    @GET("analytics/{id}")
    suspend fun getMetricById(
        @Path("id") id: Long
    ): MetricDto
}