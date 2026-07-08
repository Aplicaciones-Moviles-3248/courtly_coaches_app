package com.courtly.coaches.contexts.analytics.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courtly.coaches.contexts.analytics.application.usecases.GetMyMetricsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnalyticsViewModel (
    private val getMyMetricsUseCase: GetMyMetricsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AnalyticsUiState()
    )

    val uiState: StateFlow<AnalyticsUiState> =
        _uiState.asStateFlow()

    fun loadMetrics() {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {

                val metrics =
                    getMyMetricsUseCase()

                _uiState.value =
                    _uiState.value.copy(
                        metrics = metrics,
                        isLoading = false
                    )

            } catch (e: Exception) {

                _uiState.value =
                    _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "No fue posible cargar las estadísticas."
                    )
            }
        }
    }
}