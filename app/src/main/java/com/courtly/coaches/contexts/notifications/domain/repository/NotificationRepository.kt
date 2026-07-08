package com.courtly.coaches.contexts.notifications.domain.repository

import com.courtly.coaches.contexts.notifications.domain.model.Notification

interface NotificationRepository {
    suspend fun getMyNotifications(): List<Notification>
    suspend fun markAsRead(id: Long): Notification
    suspend fun getMyUnreadCount(): Long
}