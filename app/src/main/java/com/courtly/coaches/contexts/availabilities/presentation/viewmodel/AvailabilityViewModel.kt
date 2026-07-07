package com.courtly.coaches.contexts.availabilities.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courtly.coaches.contexts.availabilities.application.usecases.CreateAvailabilityUseCase
import com.courtly.coaches.contexts.availabilities.application.usecases.DeleteAvailabilityUseCase
import com.courtly.coaches.contexts.availabilities.application.usecases.GetAllAvailabilitiesUseCase
import com.courtly.coaches.contexts.availabilities.application.usecases.GetAvailabilityByIdUseCase
import com.courtly.coaches.contexts.availabilities.application.usecases.GetMyAvailabilitiesUseCase
import com.courtly.coaches.contexts.availabilities.application.usecases.UpdateAvailabilityUseCase
import com.courtly.coaches.contexts.availabilities.domain.model.Availability
import com.courtly.coaches.contexts.availabilities.domain.model.AvailabilityStatus
import com.courtly.coaches.contexts.availabilities.domain.model.CreateAvailabilityParams
import com.courtly.coaches.contexts.availabilities.domain.model.UpdateAvailabilityParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AvailabilityViewModel(
    private val getAllAvailabilitiesUseCase: GetAllAvailabilitiesUseCase,
    private val getMyAvailabilitiesUseCase: GetMyAvailabilitiesUseCase,
    private val getAvailabilityByIdUseCase: GetAvailabilityByIdUseCase,
    private val createAvailabilityUseCase: CreateAvailabilityUseCase,
    private val updateAvailabilityUseCase: UpdateAvailabilityUseCase,
    private val deleteAvailabilityUseCase: DeleteAvailabilityUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AvailabilityUiState())
    val uiState: StateFlow<AvailabilityUiState> = _uiState.asStateFlow()

    fun loadMyAvailabilities() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val availabilities = getMyAvailabilitiesUseCase()
                _uiState.value = _uiState.value.copy(
                    availabilities = availabilities,
                    isLoading = false
                )
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "No se pudo cargar tu agenda."
                )
            }
        }
    }

    fun loadAllAvailabilities() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val availabilities = getAllAvailabilitiesUseCase()
                _uiState.value = _uiState.value.copy(
                    availabilities = availabilities,
                    isLoading = false
                )
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "No se pudo cargar la disponibilidad."
                )
            }
        }
    }

    fun loadAvailabilityById(id: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val availability = getAvailabilityByIdUseCase(id)
                _uiState.value = _uiState.value.copy(
                    selectedAvailability = availability,
                    isLoading = false
                )
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "No se pudo cargar la disponibilidad."
                )
            }
        }
    }

    fun createAvailability(
        date: String,
        startTime: String,
        endTime: String,
        status: AvailabilityStatus,
        coachId: Long
    ) {
        if (!validateDateTime(date, startTime, endTime)) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSaving = true,
                errorMessage = null,
                operationSuccess = false
            )

            try {
                val availability = createAvailabilityUseCase(
                    CreateAvailabilityParams(
                        date = date.trim(),
                        startTime = startTime.trim(),
                        endTime = endTime.trim(),
                        status = status,
                        coachId = coachId
                    )
                )

                _uiState.value = _uiState.value.copy(
                    selectedAvailability = availability,
                    isSaving = false,
                    operationSuccess = true
                )
            } catch (error: HttpException) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessage = getErrorMessage(error)
                )
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessage = "No se pudo crear la disponibilidad."
                )
            }
        }
    }

    fun updateAvailability(
        id: Long,
        date: String,
        startTime: String,
        endTime: String,
        status: AvailabilityStatus
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSaving = true,
                errorMessage = null,
                operationSuccess = false
            )

            try {
                val updatedAvailability = updateAvailabilityUseCase(
                    id = id,
                    params = UpdateAvailabilityParams(
                        date = date.trim(),
                        startTime = startTime.trim(),
                        endTime = endTime.trim(),
                        status = status
                    )
                )

                _uiState.value = _uiState.value.copy(
                    selectedAvailability = updatedAvailability,
                    isSaving = false,
                    operationSuccess = true
                )
            } catch (error: HttpException) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessage = getErrorMessage(error)
                )
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessage = "No se pudo actualizar la disponibilidad."
                )
            }
        }
    }

    fun deleteAvailability(id: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isDeleting = true,
                errorMessage = null,
                operationSuccess = false
            )

            try {
                deleteAvailabilityUseCase(id)
                _uiState.value = _uiState.value.copy(
                    selectedAvailability = null,
                    isDeleting = false,
                    operationSuccess = true
                )
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(
                    isDeleting = false,
                    errorMessage = "No se pudo eliminar la disponibilidad."
                )
            }
        }
    }

    fun clearOperationSuccess() {
        _uiState.value = _uiState.value.copy(operationSuccess = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun clearSelectedAvailability() {
        _uiState.value = _uiState.value.copy(selectedAvailability = null)
    }

    private fun validateDateTime(
        date: String,
        startTime: String,
        endTime: String
    ): Boolean {
        val errorMessage = when {
            !DATE_REGEX.matches(date.trim()) ->
                "Ingresa una fecha válida con formato YYYY-MM-DD."

            !TIME_REGEX.matches(startTime.trim()) ->
                "Ingresa una hora de inicio válida con formato HH:mm."

            !TIME_REGEX.matches(endTime.trim()) ->
                "Ingresa una hora de fin válida con formato HH:mm."

            else -> null
        }

        if (errorMessage != null) {
            _uiState.value = _uiState.value.copy(
                isSaving = false,
                errorMessage = errorMessage
            )
            return false
        }

        return true
    }

    private fun getErrorMessage(error: HttpException): String {
        return when (error.code()) {
            400 -> "Los datos enviados no son válidos."
            401 -> "Tu sesión no es válida o ha expirado."
            403 -> "No tienes permisos para realizar esta operación."
            404 -> "No se encontró la disponibilidad."
            409 -> "Ya existe una disponibilidad en ese horario."
            else -> "Ocurrió un error al comunicarse con el servidor."
        }
    }

    companion object {
        private val DATE_REGEX = Regex("^\\d{4}-\\d{2}-\\d{2}$")
        private val TIME_REGEX = Regex("^([01]\\d|2[0-3]):[0-5]\\d$")
    }
}
