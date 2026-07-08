package com.courtly.coaches.contexts.notifications.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.courtly.coaches.contexts.notifications.application.usecases.GetMyNotificationsUseCase
import com.courtly.coaches.contexts.notifications.application.usecases.GetMyUnreadCountUseCase
import com.courtly.coaches.contexts.notifications.application.usecases.MarkNotificationAsReadUseCase

class NotificationViewModelFactory (
    private val getMyNotificationsUseCase: GetMyNotificationsUseCase,
    private val markNotificationAsReadUseCase: MarkNotificationAsReadUseCase,
    private val getMyUnreadCountUseCase: GetMyUnreadCountUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            return NotificationViewModel(
                getMyNotificationsUseCase = getMyNotificationsUseCase,
                markNotificationAsReadUseCase = markNotificationAsReadUseCase,
                getMyUnreadCountUseCase = getMyUnreadCountUseCase
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}