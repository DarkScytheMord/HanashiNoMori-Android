package com.example.hanashinomori.dto

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val userId: Int? = null,
    val username: String? = null
)

