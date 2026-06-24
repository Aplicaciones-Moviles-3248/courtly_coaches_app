package com.courtly.coaches.contexts.iam.application.usecases

import com.courtly.coaches.contexts.iam.domain.repository.AuthenticationRepository

class SignOutUseCase(
    private val repository: AuthenticationRepository
) {
    operator fun invoke() {
        repository.signOut()
    }
}