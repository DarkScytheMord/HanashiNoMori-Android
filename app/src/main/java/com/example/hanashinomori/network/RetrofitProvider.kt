package com.example.hanashinomori.network

import com.example.hanashinomori.api.ApiService
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitProvider {

    private val client by lazy {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logger)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val json by lazy {
        Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")  // ✅ Backend local (emulador Android)
            // Si usas dispositivo físico, cambia a: "http://TU_IP:8080/"
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // ✅ Propiedad apiService para acceso directo
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    fun <T> create(service: Class<T>): T = retrofit.create(service)
    inline fun <reified T> create(): T = retrofit.create(T::class.java)
}
