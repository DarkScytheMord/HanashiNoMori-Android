package com.example.hanashinomori.model

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Authenticated : AuthState()  // Usuario autenticado
    data class Success(val username: String) : AuthState()  // Para compatibilidad
    data class Error(val message: String) : AuthState()
}

