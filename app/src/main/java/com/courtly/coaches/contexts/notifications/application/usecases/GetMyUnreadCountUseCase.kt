package com.courtly.coaches.contexts.notifications.application.usecases

import com.courtly.coaches.contexts.notifications.domain.repository.NotificationRepository

class GetMyUnreadCountUseCase (
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(): Long = repository.getMyUnreadCount()
}