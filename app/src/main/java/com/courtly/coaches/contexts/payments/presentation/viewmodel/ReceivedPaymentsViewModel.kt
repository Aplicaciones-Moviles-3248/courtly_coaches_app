package com.courtly.coaches.contexts.payments.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courtly.coaches.contexts.payments.application.usecases.GetReceivedPaymentsUseCase
import com.courtly.coaches.contexts.payments.infrastructure.repository.PaymentEndpointUnavailableException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ReceivedPaymentsViewModel(
    private val getReceivedPaymentsUseCase: GetReceivedPaymentsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReceivedPaymentsUiState())
    val uiState: StateFlow<ReceivedPaymentsUiState> = _uiState.asStateFlow()

    fun loadReceivedPayments() {
        viewModelScope.launch {
            Log.d("USM15", "[USM15] Loading received payments")
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val payments = getReceivedPaymentsUseCase()
                Log.d("USM15", "[USM15] Received payments count = ${payments.size}")
                if (payments.isEmpty()) {
                    Log.d("USM15", "[USM15] Empty received payments list")
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    payments = payments
                )
            } catch (e: Exception) {
                Log.e("USM15", "[USM15] Error loading received payments = ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    payments = emptyList(),
                    errorMessage = buildErrorMessage(e)
                )
            }
        }
    }

    private fun buildErrorMessage(error: Exception): String {
        val detail = when (error) {
            is PaymentEndpointUnavailableException ->
                "Verifica tu sesión e intenta nuevamente. El endpoint de pagos recibidos no está disponible en el backend."
            is HttpException ->
                when (error.code()) {
                    401, 403 -> "Verifica tu sesión e intenta nuevamente."
                    else -> "Verifica tu sesión e intenta nuevamente."
                }
            else -> "Verifica tu sesión e intenta nuevamente."
        }

        return "No se pudieron cargar los pagos recibidos.\n$detail"
    }
}
