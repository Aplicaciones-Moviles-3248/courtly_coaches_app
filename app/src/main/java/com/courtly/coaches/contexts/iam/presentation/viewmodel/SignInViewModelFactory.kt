package com.courtly.coaches.contexts.iam.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.courtly.coaches.contexts.coaches.application.usecases.CreateCoachUseCase
import com.courtly.coaches.contexts.iam.application.usecases.SignInUseCase
import com.courtly.coaches.contexts.iam.application.usecases.SignUpUseCase

class SignInViewModelFactory(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val createCoachUseCase: CreateCoachUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(
                signInUseCase = signInUseCase,
                signUpUseCase = signUpUseCase,
                createCoachUseCase = createCoachUseCase
            ) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido: ${modelClass.name}"
        )
    }
}