package com.courtly.coaches.contexts.notifications.infrastructure.repository

import com.courtly.coaches.contexts.notifications.domain.model.Notification
import com.courtly.coaches.contexts.notifications.domain.repository.NotificationRepository
import com.courtly.coaches.contexts.notifications.infrastructure.remote.NotificationApiService

class NotificationRepositoryImpl (
    private val apiService: NotificationApiService
) : NotificationRepository {

    override suspend fun getMyNotifications(): List<Notification> {
        val response = apiService.getMyNotifications()
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.map { it.toDomain() }
        }
        throw retrofit2.HttpException(response)
    }

    override suspend fun markAsRead(id: Long): Notification {
        val response = apiService.markNotificationAsRead(id)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.toDomain()
        }
        throw retrofit2.HttpException(response)
    }

    override suspend fun getMyUnreadCount(): Long {
        val response = apiService.getMyUnreadCount()
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.unreadCount
        }
        throw retrofit2.HttpException(response)
    }
}