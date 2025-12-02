package com.example.hanashinomori.dto

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

// Alias para mantener compatibilidad
typealias RegisterDto = RegisterRequest

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val userId: Long? = null,
    val username: String? = null,
    val email: String? = null,
    val token: String? = null
)

