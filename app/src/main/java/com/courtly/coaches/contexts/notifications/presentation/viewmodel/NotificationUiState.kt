package com.courtly.coaches.contexts.notifications.presentation.viewmodel

import com.courtly.coaches.contexts.notifications.domain.model.Notification

data class NotificationUiState (
    val notifications: List<Notification> = emptyList(),
    val unreadCount: Long = 0L,
    val isLoading: Boolean = false,
    val isUpdating: Boolean = false,
    val operationSuccess: Boolean = false,
    val errorMessage: String? = null
)