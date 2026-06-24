package com.courtly.coaches.contexts.coaches.presentation.viewmodel

import com.courtly.coaches.contexts.coaches.domain.model.Coach

data class CoachUiState(
    val coach: Coach? = null,
    val coaches: List<Coach> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val profileNotFound: Boolean = false,
    val operationSuccess: Boolean = false,
    val errorMessage: String? = null
)