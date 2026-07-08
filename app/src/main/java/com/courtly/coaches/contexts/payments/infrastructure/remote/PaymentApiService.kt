package com.courtly.coaches.contexts.payments.infrastructure.remote

import com.courtly.coaches.contexts.payments.infrastructure.model.PaymentDto
import retrofit2.http.GET

interface PaymentApiService {
    @GET("payments/received")
    suspend fun getReceivedPayments(): List<PaymentDto>
}
