package com.courtly.coaches.shared.infrastructure.network

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * Simple app-wide event bus used to notify the UI layer that the current
 * session has expired (e.g. the backend responded with 401 to an
 * authenticated request) and the user must be sent back to sign-in.
 */
object SessionEventBus {

    private val _sessionExpiredEvents =
        MutableSharedFlow<Unit>(
            extraBufferCapacity = 1
        )

    val sessionExpiredEvents: SharedFlow<Unit> = _sessionExpiredEvents

    fun notifySessionExpired() {
        _sessionExpiredEvents.tryEmit(Unit)
    }
}
