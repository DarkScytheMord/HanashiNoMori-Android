package com.example.hanashinomori.dto

data class LoginRequest(
    val usernameOrEmail: String,
    val password: String
)

// Alias para mantener compatibilidad
typealias LoginDto = LoginRequest

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val userId: Long? = null,
    val username: String? = null,
    val email: String? = null,
    val token: String? = null
)

