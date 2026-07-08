package com.courtly.coaches.contexts.coaches.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courtly.coaches.contexts.coaches.application.usecases.CreateCoachUseCase
import com.courtly.coaches.contexts.coaches.application.usecases.DeleteCoachUseCase
import com.courtly.coaches.contexts.coaches.application.usecases.GetAllCoachesUseCase
import com.courtly.coaches.contexts.coaches.application.usecases.GetMyCoachUseCase
import com.courtly.coaches.contexts.coaches.application.usecases.UpdateCoachUseCase
import com.courtly.coaches.contexts.coaches.domain.model.CreateCoachParams
import com.courtly.coaches.contexts.coaches.domain.model.UpdateCoachParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CoachViewModel(
    private val getMyCoachUseCase: GetMyCoachUseCase,
    private val getAllCoachesUseCase: GetAllCoachesUseCase,
    private val createCoachUseCase: CreateCoachUseCase,
    private val updateCoachUseCase: UpdateCoachUseCase,
    private val deleteCoachUseCase: DeleteCoachUseCase,
    private val getCoachReviewsUseCase: com.courtly.coaches.contexts.reviews.application.usecases.GetCoachReviewsUseCase? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(CoachUiState())
    val uiState: StateFlow<CoachUiState> = _uiState.asStateFlow()

    fun loadMyCoach() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                profileNotFound = false,
                errorMessage = null
            )

            try {
                val coach = getMyCoachUseCase()
                val reviews = getCoachReviewsUseCase?.execute(coach.id) ?: emptyList()

                _uiState.value = _uiState.value.copy(
                    coach = coach,
                    reviews = reviews,
                    isLoading = false,
                    profileNotFound = false
                )
            } catch (error: HttpException) {
                if (error.code() == 404 || error.code() == 401) {
                    _uiState.value = _uiState.value.copy(
                        coach = null,
                        isLoading = false,
                        profileNotFound = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = getErrorMessage(error)
                    )
                }
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "No se pudo cargar el perfil del entrenador."
                )
            }
        }
    }

    fun loadAllCoaches() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val coaches = getAllCoachesUseCase()

                _uiState.value = _uiState.value.copy(
                    coaches = coaches,
                    isLoading = false
                )
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "No se pudo cargar la lista de entrenadores."
                )
            }
        }
    }

    fun createCoach(
        name: String,
        expertise: String,
        phone: String,
        userId: Long
    ) {
        if (!validateFields(name, expertise, phone)) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSaving = true,
                errorMessage = null,
                operationSuccess = false
            )

            try {
                val coach = createCoachUseCase(
                    CreateCoachParams(
                        name = name.trim(),
                        expertise = expertise.trim(),
                        phone = phone.trim(),
                        userId = userId
                    )
                )

                _uiState.value = _uiState.value.copy(
                    coach = coach,
                    isSaving = false,
                    profileNotFound = false,
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
                    errorMessage = "No se pudo crear el perfil del entrenador."
                )
            }
        }
    }

    fun updateCoach(
        id: Long,
        name: String,
        expertise: String,
        phone: String
    ) {
        if (!validateFields(name, expertise, phone)) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSaving = true,
                errorMessage = null,
                operationSuccess = false
            )

            try {
                val updatedCoach = updateCoachUseCase(
                    id = id,
                    params = UpdateCoachParams(
                        name = name.trim(),
                        expertise = expertise.trim(),
                        phone = phone.trim()
                    )
                )

                _uiState.value = _uiState.value.copy(
                    coach = updatedCoach,
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
                    errorMessage = "No se pudo actualizar el perfil."
                )
            }
        }
    }

    fun deleteCoach(id: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isDeleting = true,
                errorMessage = null,
                operationSuccess = false
            )

            try {
                deleteCoachUseCase(id)

                _uiState.value = _uiState.value.copy(
                    coach = null,
                    isDeleting = false,
                    profileNotFound = true,
                    operationSuccess = true
                )
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(
                    isDeleting = false,
                    errorMessage = "No se pudo eliminar el perfil."
                )
            }
        }
    }

    fun clearOperationSuccess() {
        _uiState.value = _uiState.value.copy(
            operationSuccess = false
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null
        )
    }

    private fun validateFields(
        name: String,
        expertise: String,
        phone: String
    ): Boolean {
        val errorMessage = when {
            name.trim().length < 3 ->
                "Ingresa un nombre válido."

            expertise.trim().length < 3 ->
                "Ingresa una especialidad válida."

            phone.trim().length < 7 ->
                "Ingresa un teléfono válido."

            else -> null
        }

        if (errorMessage != null) {
            _uiState.value = _uiState.value.copy(
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
            404 -> "No se encontró el perfil del entrenador."
            409 -> "Ya existe un perfil asociado a este usuario."
            else -> "Ocurrió un error al comunicarse con el servidor."
        }
    }
}