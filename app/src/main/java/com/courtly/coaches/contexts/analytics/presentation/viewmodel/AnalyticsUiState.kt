package com.courtly.coaches.contexts.analytics.presentation.viewmodel

import com.courtly.coaches.contexts.analytics.domain.model.Metric

data class AnalyticsUiState (
    val metrics: List<Metric> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)