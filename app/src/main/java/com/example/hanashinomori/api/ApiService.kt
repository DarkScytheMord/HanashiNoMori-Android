package com.example.hanashinomori.api

import com.example.hanashinomori.dto.AuthResponse
import com.example.hanashinomori.dto.LoginRequest
import com.example.hanashinomori.dto.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/auth/register")  // POST http://10.0.2.2:8080/api/auth/register
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/login")  // POST http://10.0.2.2:8080/api/auth/login
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
}

