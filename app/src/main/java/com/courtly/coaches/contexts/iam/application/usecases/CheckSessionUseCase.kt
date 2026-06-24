package com.courtly.coaches.contexts.iam.application.usecases

import com.courtly.coaches.contexts.iam.domain.repository.AuthenticationRepository

class CheckSessionUseCase(
    private val repository: AuthenticationRepository
) {
    operator fun invoke(): Boolean {
        return repository.hasActiveSession()
    }
}