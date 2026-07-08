package com.courtly.coaches.contexts.analytics.application.usecases

import com.courtly.coaches.contexts.analytics.domain.repository.AnalyticsRepository

class GetMyMetricsUseCase (
    private val repository: AnalyticsRepository
) {

    suspend operator fun invoke() =
        repository.getMyMetrics()
}