package com.courtly.coaches.contexts.iam.application.usecases

import com.courtly.coaches.contexts.iam.domain.model.AuthenticatedUser
import com.courtly.coaches.contexts.iam.domain.repository.AuthenticationRepository

class SignInUseCase(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(
        username: String,
        password: String
    ): AuthenticatedUser {
        return repository.signIn(username, password)
    }
}