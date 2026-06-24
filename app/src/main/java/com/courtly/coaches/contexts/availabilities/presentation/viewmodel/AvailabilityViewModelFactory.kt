package com.courtly.coaches.contexts.availabilities.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.courtly.coaches.contexts.availabilities.application.usecases.CreateAvailabilityUseCase
import com.courtly.coaches.contexts.availabilities.application.usecases.DeleteAvailabilityUseCase
import com.courtly.coaches.contexts.availabilities.application.usecases.GetAllAvailabilitiesUseCase
import com.courtly.coaches.contexts.availabilities.application.usecases.GetAvailabilityByIdUseCase
import com.courtly.coaches.contexts.availabilities.application.usecases.GetMyAvailabilitiesUseCase
import com.courtly.coaches.contexts.availabilities.application.usecases.UpdateAvailabilityUseCase

class AvailabilityViewModelFactory(
    private val getAllAvailabilitiesUseCase: GetAllAvailabilitiesUseCase,
    private val getMyAvailabilitiesUseCase: GetMyAvailabilitiesUseCase,
    private val getAvailabilityByIdUseCase: GetAvailabilityByIdUseCase,
    private val createAvailabilityUseCase: CreateAvailabilityUseCase,
    private val updateAvailabilityUseCase: UpdateAvailabilityUseCase,
    private val deleteAvailabilityUseCase: DeleteAvailabilityUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(AvailabilityViewModel::class.java)) {
            return AvailabilityViewModel(
                getAllAvailabilitiesUseCase = getAllAvailabilitiesUseCase,
                getMyAvailabilitiesUseCase = getMyAvailabilitiesUseCase,
                getAvailabilityByIdUseCase = getAvailabilityByIdUseCase,
                createAvailabilityUseCase = createAvailabilityUseCase,
                updateAvailabilityUseCase = updateAvailabilityUseCase,
                deleteAvailabilityUseCase = deleteAvailabilityUseCase
            ) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido: ${modelClass.name}"
        )
    }
}
