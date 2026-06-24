package com.courtly.coaches.shared.infrastructure.storage

import android.content.Context

class SessionStorage(
    context: Context
) {
    private val preferences = context.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    fun saveSession(
        userId: Int,
        username: String,
        token: String
    ) {
        preferences.edit()
            .putInt(USER_ID_KEY, userId)
            .putString(USERNAME_KEY, username)
            .putString(TOKEN_KEY, token)
            .apply()
    }

    fun getToken(): String? {
        return preferences.getString(TOKEN_KEY, null)
    }

    fun getUserId(): Int? {
        val userId = preferences.getInt(USER_ID_KEY, -1)
        return if (userId == -1) null else userId
    }

    fun getUsername(): String? {
        return preferences.getString(USERNAME_KEY, null)
    }

    fun hasActiveSession(): Boolean {
        return !getToken().isNullOrBlank()
    }

    fun clearSession() {
        preferences.edit()
            .remove(USER_ID_KEY)
            .remove(USERNAME_KEY)
            .remove(TOKEN_KEY)
            .apply()
    }

    companion object {
        private const val PREFERENCES_NAME = "courtly_session"
        private const val USER_ID_KEY = "auth_user_id"
        private const val USERNAME_KEY = "auth_username"
        private const val TOKEN_KEY = "auth_token"
    }
}