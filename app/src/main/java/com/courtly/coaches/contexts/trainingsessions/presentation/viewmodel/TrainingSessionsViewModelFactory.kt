package com.courtly.coaches.contexts.trainingsessions.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.courtly.coaches.contexts.trainingsessions.application.usecases.AcceptTrainingSessionUseCase
import com.courtly.coaches.contexts.trainingsessions.application.usecases.GetMyTrainingSessionsUseCase
import com.courtly.coaches.contexts.trainingsessions.application.usecases.RejectTrainingSessionUseCase

class TrainingSessionsViewModelFactory(
    private val getMyTrainingSessionsUseCase: GetMyTrainingSessionsUseCase,
    private val acceptTrainingSessionUseCase: AcceptTrainingSessionUseCase,
    private val rejectTrainingSessionUseCase: RejectTrainingSessionUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrainingSessionsViewModel::class.java)) {
            return TrainingSessionsViewModel(
                getMyTrainingSessionsUseCase,
                acceptTrainingSessionUseCase,
                rejectTrainingSessionUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
