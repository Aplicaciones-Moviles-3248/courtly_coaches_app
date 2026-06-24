package com.courtly.coaches.shared.infrastructure.network

import com.courtly.coaches.shared.infrastructure.storage.SessionStorage
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val sessionStorage: SessionStorage
) : Interceptor {

    override fun intercept(
        chain: Interceptor.Chain
    ): Response {
        val originalRequest = chain.request()
        val path = originalRequest.url.encodedPath

        val isPublicEndpoint =
            path.endsWith("/authentication/sign-in") ||
                    path.endsWith("/authentication/sign-up")

        if (isPublicEndpoint) {
            return chain.proceed(originalRequest)
        }

        val token = sessionStorage.getToken()

        if (token.isNullOrBlank()) {
            return chain.proceed(originalRequest)
        }

        val authenticatedRequest =
            originalRequest
                .newBuilder()
                .header(
                    "Authorization",
                    "Bearer $token"
                )
                .build()

        return chain.proceed(authenticatedRequest)
    }
}