package com.courtly.coaches.contexts.trainingsessions.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courtly.coaches.contexts.trainingsessions.application.usecases.AcceptTrainingSessionUseCase
import com.courtly.coaches.contexts.trainingsessions.application.usecases.GetMyTrainingSessionsUseCase
import com.courtly.coaches.contexts.trainingsessions.application.usecases.RejectTrainingSessionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrainingSessionsViewModel(
    private val getMyTrainingSessionsUseCase: GetMyTrainingSessionsUseCase,
    private val acceptTrainingSessionUseCase: AcceptTrainingSessionUseCase,
    private val rejectTrainingSessionUseCase: RejectTrainingSessionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TrainingSessionsUiState())
    val uiState: StateFlow<TrainingSessionsUiState> = _uiState.asStateFlow()

    fun loadMyTrainingSessions() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )
            try {
                val sessions = getMyTrainingSessionsUseCase.execute()
                _uiState.value = _uiState.value.copy(
                    sessions = sessions,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al cargar las sesiones: ${e.message}"
                )
            }
        }
    }

    fun acceptTrainingSession(id: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUpdating = true)
            try {
                acceptTrainingSessionUseCase.execute(id)
                _uiState.value = _uiState.value.copy(
                    isUpdating = false,
                    operationSuccess = true
                )
                loadMyTrainingSessions()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isUpdating = false,
                    errorMessage = "No se pudo aceptar la solicitud: ${e.message}"
                )
            }
        }
    }

    fun rejectTrainingSession(id: Long, reason: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUpdating = true)
            try {
                rejectTrainingSessionUseCase.execute(id, reason)
                _uiState.value = _uiState.value.copy(
                    isUpdating = false,
                    operationSuccess = true
                )
                loadMyTrainingSessions()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isUpdating = false,
                    errorMessage = "No se pudo rechazar la solicitud: ${e.message}"
                )
            }
        }
    }
}
