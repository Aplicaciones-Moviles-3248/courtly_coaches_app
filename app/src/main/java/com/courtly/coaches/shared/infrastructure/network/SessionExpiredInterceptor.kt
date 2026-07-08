package com.courtly.coaches.shared.infrastructure.network

import com.courtly.coaches.shared.infrastructure.storage.SessionStorage
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Response-side interceptor that detects an expired/invalid session token.
 *
 * When an authenticated request (one that carries an `Authorization` header,
 * added upstream by [AuthInterceptor]) comes back with HTTP 401, the stored
 * session is no longer valid on the backend, so we clear it locally and
 * notify the UI layer (via [SessionEventBus]) to route the user back to
 * sign-in.
 *
 * A 401 on a request that never carried an `Authorization` header (e.g. a
 * failed sign-in attempt with bad credentials) is left untouched, since that
 * simply means "wrong credentials", not "session expired".
 *
 * This interceptor must be registered *after* [AuthInterceptor] in the
 * OkHttp client so that [Interceptor.Chain.request] reflects the request as
 * actually sent (including the `Authorization` header, if any).
 */
class SessionExpiredInterceptor(
    private val sessionStorage: SessionStorage
) : Interceptor {

    override fun intercept(
        chain: Interceptor.Chain
    ): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        val wasAuthenticatedRequest =
            request.header("Authorization") != null

        if (response.code == 401 && wasAuthenticatedRequest) {
            sessionStorage.clearSession()
            SessionEventBus.notifySessionExpired()
        }

        return response
    }
}
