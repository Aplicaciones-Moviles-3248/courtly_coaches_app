package com.courtly.coaches.contexts.notifications.application.usecases

import com.courtly.coaches.contexts.notifications.domain.model.Notification
import com.courtly.coaches.contexts.notifications.domain.repository.NotificationRepository

class MarkNotificationAsReadUseCase (
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(id: Long): Notification = repository.markAsRead(id)
}