package com.courtly.coaches.contexts.availabilities.presentation.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.courtly.coaches.contexts.availabilities.application.usecases.CreateAvailabilityUseCase
import com.courtly.coaches.contexts.availabilities.application.usecases.DeleteAvailabilityUseCase
import com.courtly.coaches.contexts.availabilities.application.usecases.GetAllAvailabilitiesUseCase
import com.courtly.coaches.contexts.availabilities.application.usecases.GetAvailabilityByIdUseCase
import com.courtly.coaches.contexts.availabilities.application.usecases.GetMyAvailabilitiesUseCase
import com.courtly.coaches.contexts.availabilities.application.usecases.UpdateAvailabilityUseCase
import com.courtly.coaches.contexts.availabilities.infrastructure.remote.AvailabilityApiService
import com.courtly.coaches.contexts.availabilities.infrastructure.repository.AvailabilityRepositoryImpl
import com.courtly.coaches.contexts.availabilities.presentation.viewmodel.AvailabilityViewModel
import com.courtly.coaches.contexts.availabilities.presentation.viewmodel.AvailabilityViewModelFactory
import com.courtly.coaches.shared.infrastructure.network.RetrofitClient

@Composable
fun CoachAvailabilityScreen(
    coachId: Long
) {
    val apiService = RetrofitClient.retrofit.create(
        AvailabilityApiService::class.java
    )

    val repository = AvailabilityRepositoryImpl(apiService)
    val viewModel: AvailabilityViewModel = viewModel(
        factory = AvailabilityViewModelFactory(
            getAllAvailabilitiesUseCase = GetAllAvailabilitiesUseCase(repository),
            getMyAvailabilitiesUseCase = GetMyAvailabilitiesUseCase(repository),
            getAvailabilityByIdUseCase = GetAvailabilityByIdUseCase(repository),
            createAvailabilityUseCase = CreateAvailabilityUseCase(repository),
            updateAvailabilityUseCase = UpdateAvailabilityUseCase(repository),
            deleteAvailabilityUseCase = DeleteAvailabilityUseCase(repository)
        )
    )

    AvailabilityScreen(
        viewModel = viewModel,
        coachId = coachId
    )
}
