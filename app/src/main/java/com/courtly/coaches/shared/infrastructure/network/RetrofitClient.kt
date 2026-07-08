package com.courtly.coaches.shared.infrastructure.network

import com.courtly.coaches.shared.infrastructure.storage.SessionStorage
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL =
        "https://backend-production-d772.up.railway.app/api/v1/"

    private lateinit var sessionStorage: SessionStorage

    private var retrofitInstance: Retrofit? = null

    fun initialize(sessionStorage: SessionStorage) {
        this.sessionStorage = sessionStorage

        retrofitInstance = createRetrofit()
    }

    val retrofit: Retrofit
        get() {
            check(::sessionStorage.isInitialized) {
                "RetrofitClient debe inicializarse con SessionStorage antes de utilizarse."
            }

            return retrofitInstance ?: createRetrofit().also {
                retrofitInstance = it
            }
        }

    private fun createRetrofit(): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                AuthInterceptor(sessionStorage)
            )
            .addInterceptor(
                SessionExpiredInterceptor(sessionStorage)
            )
            .connectTimeout(
                30,
                TimeUnit.SECONDS
            )
            .readTimeout(
                60,
                TimeUnit.SECONDS
            )
            .writeTimeout(
                30,
                TimeUnit.SECONDS
            )
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }
}
