package com.courtly.coaches.contexts.payments.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.courtly.coaches.contexts.payments.application.usecases.GetReceivedPaymentsUseCase

class ReceivedPaymentsViewModelFactory(
    private val getReceivedPaymentsUseCase: GetReceivedPaymentsUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReceivedPaymentsViewModel::class.java)) {
            return ReceivedPaymentsViewModel(getReceivedPaymentsUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
