package com.courtly.coaches.contexts.analytics.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.courtly.coaches.contexts.analytics.application.usecases.GetMyMetricsUseCase

class AnalyticsViewModelFactory (
    private val getMyMetricsUseCase: GetMyMetricsUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(AnalyticsViewModel::class.java)) {

            return AnalyticsViewModel(
                getMyMetricsUseCase
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class"
        )
    }
}