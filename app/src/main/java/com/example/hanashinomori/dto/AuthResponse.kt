package com.example.hanashinomori.dto

import com.google.gson.annotations.SerializedName

// Response unificada para Login y Register
data class AuthResponse(
    val success: Boolean,
    val message: String,
    val data: UserData? = null,

    // Campos opcionales para compatibilidad directa
    val userId: Long? = null,
    val username: String? = null,
    val email: String? = null,
    val token: String? = null
)

// Datos del usuario dentro de "data"
data class UserData(
    @SerializedName("userId")  // Backend devuelve "userId", no "id"
    val userId: Long? = null,
    val username: String? = null,
    val email: String? = null,
    val token: String? = null
)

