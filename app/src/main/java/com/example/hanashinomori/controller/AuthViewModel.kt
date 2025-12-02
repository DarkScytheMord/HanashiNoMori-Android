package com.example.hanashinomori.controller

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hanashinomori.model.AuthState
import com.example.hanashinomori.repository.AuthRepository
import com.example.hanashinomori.repository.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val TAG = "AuthViewModel"

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<UserSession?>(null)
    val currentUser: StateFlow<UserSession?> = _currentUser.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        // Verificar si hay sesión activa
        _currentUser.value = authRepository.getCurrentSession()
        if (authRepository.isLoggedIn()) {
            _authState.value = AuthState.Authenticated
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _errorMessage.value = null

            Log.d(TAG, "🔐 Intentando registro...")

            authRepository.register(username, email, password)
                .onSuccess { session ->
                    _currentUser.value = session
                    _authState.value = AuthState.Authenticated
                    Log.d(TAG, "✅ Registro exitoso - Usuario: ${session.username}")
                }
                .onFailure { error ->
                    _authState.value = AuthState.Error(error.message ?: "Error desconocido")
                    _errorMessage.value = error.message
                    Log.e(TAG, "❌ Error en registro: ${error.message}")
                }
        }
    }

    fun login(usernameOrEmail: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _errorMessage.value = null

            Log.d(TAG, "🔐 Intentando login...")

            authRepository.login(usernameOrEmail, password)
                .onSuccess { session ->
                    _currentUser.value = session
                    _authState.value = AuthState.Authenticated
                    Log.d(TAG, "✅ Login exitoso - Usuario: ${session.username}")
                }
                .onFailure { error ->
                    _authState.value = AuthState.Error(error.message ?: "Credenciales incorrectas")
                    _errorMessage.value = error.message
                    Log.e(TAG, "❌ Error en login: ${error.message}")
                }
        }
    }

    fun logout() {
        authRepository.logout()
        _currentUser.value = null
        _authState.value = AuthState.Idle
        Log.d(TAG, "🚪 Sesión cerrada")
    }

    fun clearError() {
        _errorMessage.value = null
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.Idle
        }
    }

    fun getUserId(): Long? = _currentUser.value?.userId

    fun getUsername(): String? = _currentUser.value?.username
}

