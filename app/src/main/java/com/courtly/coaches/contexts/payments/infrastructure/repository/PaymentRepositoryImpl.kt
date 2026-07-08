package com.courtly.coaches.contexts.payments.infrastructure.repository

import android.util.Log
import com.courtly.coaches.contexts.payments.domain.model.Payment
import com.courtly.coaches.contexts.payments.domain.repository.PaymentRepository
import com.courtly.coaches.contexts.payments.infrastructure.remote.PaymentApiService
import retrofit2.HttpException

class PaymentEndpointUnavailableException(
    cause: Throwable
) : Exception("El endpoint /payments/received no está disponible.", cause)

class PaymentRepositoryImpl(
    private val apiService: PaymentApiService
) : PaymentRepository {

    override suspend fun getReceivedPayments(): List<Payment> {
        return try {
            apiService.getReceivedPayments().map { it.toDomain() }
        } catch (e: HttpException) {
            if (e.code() == 404) {
                Log.e(
                    "USM15",
                    "[USM15] /payments/received endpoint not available in backend.",
                    e
                )
                throw PaymentEndpointUnavailableException(e)
            }
            throw e
        }
    }
}
