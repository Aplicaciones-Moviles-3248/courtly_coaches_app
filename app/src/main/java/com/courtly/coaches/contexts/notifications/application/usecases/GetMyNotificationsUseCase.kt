package com.courtly.coaches.contexts.notifications.application.usecases

import com.courtly.coaches.contexts.notifications.domain.model.Notification
import com.courtly.coaches.contexts.notifications.domain.repository.NotificationRepository

class GetMyNotificationsUseCase (
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(): List<Notification> = repository.getMyNotifications()
}