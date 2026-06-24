package com.courtly.coaches.contexts.availabilities.presentation.viewmodel

import com.courtly.coaches.contexts.availabilities.domain.model.Availability

data class AvailabilityUiState(
    val availabilities: List<Availability> = emptyList(),
    val selectedAvailability: Availability? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val operationSuccess: Boolean = false,
    val errorMessage: String? = null
)
