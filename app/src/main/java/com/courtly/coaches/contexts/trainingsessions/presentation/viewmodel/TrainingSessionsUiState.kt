package com.courtly.coaches.contexts.trainingsessions.presentation.viewmodel

import com.courtly.coaches.contexts.trainingsessions.domain.model.TrainingSession

data class TrainingSessionsUiState(
    val sessions: List<TrainingSession> = emptyList(),
    val isLoading: Boolean = false,
    val isUpdating: Boolean = false,
    val operationSuccess: Boolean = false,
    val errorMessage: String? = null
)
