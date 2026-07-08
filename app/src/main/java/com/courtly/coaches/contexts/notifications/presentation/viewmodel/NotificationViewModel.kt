package com.courtly.coaches.contexts.notifications.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courtly.coaches.contexts.notifications.application.usecases.GetMyNotificationsUseCase
import com.courtly.coaches.contexts.notifications.application.usecases.GetMyUnreadCountUseCase
import com.courtly.coaches.contexts.notifications.application.usecases.MarkNotificationAsReadUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class NotificationViewModel (
    private val getMyNotificationsUseCase: GetMyNotificationsUseCase,
    private val markNotificationAsReadUseCase: MarkNotificationAsReadUseCase,
    private val getMyUnreadCountUseCase: GetMyUnreadCountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val notifications = getMyNotificationsUseCase()
                val unreadCount = getMyUnreadCountUseCase()

                _uiState.value = _uiState.value.copy(
                    notifications = notifications,
                    unreadCount = unreadCount,
                    isLoading = false
                )
            } catch (error: HttpException) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = getErrorMessage(error)
                )
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "No se pudieron cargar las notificaciones."
                )
            }
        }
    }

    fun markAsRead(id: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isUpdating = true,
                errorMessage = null,
                operationSuccess = false
            )

            try {
                markNotificationAsReadUseCase(id)

                _uiState.value = _uiState.value.copy(
                    isUpdating = false,
                    operationSuccess = true
                )
                loadNotifications()
            } catch (error: HttpException) {
                _uiState.value = _uiState.value.copy(
                    isUpdating = false,
                    errorMessage = getErrorMessage(error)
                )
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(
                    isUpdating = false,
                    errorMessage = "No se pudo actualizar el estado de la notificación."
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

    private fun getErrorMessage(error: HttpException): String {
        return when (error.code()) {
            400 -> "La solicitud de notificación no es válida."
            401 -> "Tu sesión no es válida o ha expirado."
            403 -> "No tienes permisos para ver estas notificaciones."
            404 -> "No se encontraron registros de notificaciones."
            else -> "Ocurrió un error al comunicarse con el servidor."
        }
    }
}