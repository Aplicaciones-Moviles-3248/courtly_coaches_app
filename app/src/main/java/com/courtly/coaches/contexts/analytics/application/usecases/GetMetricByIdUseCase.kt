package com.courtly.coaches.contexts.analytics.application.usecases

import com.courtly.coaches.contexts.analytics.domain.repository.AnalyticsRepository

class GetMetricByIdUseCase (
    private val repository: AnalyticsRepository
) {

    suspend operator fun invoke(
        id: Long
    ) = repository.getMetricById(id)
}