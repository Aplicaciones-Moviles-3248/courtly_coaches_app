package com.courtly.coaches.contexts.payments.application.usecases

import com.courtly.coaches.contexts.payments.domain.repository.PaymentRepository

class GetReceivedPaymentsUseCase(
    private val repository: PaymentRepository
) {
    suspend operator fun invoke() =
        repository.getReceivedPayments()
}
