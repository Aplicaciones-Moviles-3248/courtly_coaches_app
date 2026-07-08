package com.courtly.coaches.contexts.notifications.infrastructure.remote

import com.courtly.coaches.contexts.notifications.infrastructure.model.NotificationCountDto
import com.courtly.coaches.contexts.notifications.infrastructure.model.NotificationDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NotificationApiService {

    @GET("notifications/me")
    suspend fun getMyNotifications(): Response<List<NotificationDto>>

    @POST("notifications/{id}/read")
    suspend fun markNotificationAsRead(@Path("id") id: Long): Response<NotificationDto>

    @GET("notifications/me/unread-count")
    suspend fun getMyUnreadCount(): Response<NotificationCountDto>
}