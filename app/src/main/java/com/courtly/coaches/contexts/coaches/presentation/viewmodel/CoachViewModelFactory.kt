package com.courtly.coaches.contexts.coaches.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.courtly.coaches.contexts.coaches.application.usecases.CreateCoachUseCase
import com.courtly.coaches.contexts.coaches.application.usecases.DeleteCoachUseCase
import com.courtly.coaches.contexts.coaches.application.usecases.GetAllCoachesUseCase
import com.courtly.coaches.contexts.coaches.application.usecases.GetMyCoachUseCase
import com.courtly.coaches.contexts.coaches.application.usecases.UpdateCoachUseCase

class CoachViewModelFactory(
    private val getMyCoachUseCase: GetMyCoachUseCase,
    private val getAllCoachesUseCase: GetAllCoachesUseCase,
    private val createCoachUseCase: CreateCoachUseCase,
    private val updateCoachUseCase: UpdateCoachUseCase,
    private val deleteCoachUseCase: DeleteCoachUseCase,
    private val getCoachReviewsUseCase: com.courtly.coaches.contexts.reviews.application.usecases.GetCoachReviewsUseCase? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(CoachViewModel::class.java)) {
            return CoachViewModel(
                getMyCoachUseCase = getMyCoachUseCase,
                getAllCoachesUseCase = getAllCoachesUseCase,
                createCoachUseCase = createCoachUseCase,
                updateCoachUseCase = updateCoachUseCase,
                deleteCoachUseCase = deleteCoachUseCase,
                getCoachReviewsUseCase = getCoachReviewsUseCase
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}