package com.example.hanashinomori.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hanashinomori.model.AuthState
import com.example.hanashinomori.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _currentUsername = MutableStateFlow<String?>(null)
    val currentUsername: StateFlow<String?> = _currentUsername

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = authRepository.login(username, password)

            _authState.value = if (result.isSuccess) {
                _currentUsername.value = result.getOrNull()?.username
                AuthState.Success(result.getOrNull()?.username ?: "")
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val result = authRepository.register(username, email, password)

            _authState.value = if (result.isSuccess) {
                AuthState.Success("Usuario registrado exitosamente")
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }

    fun logout() {
        _currentUsername.value = null
        _authState.value = AuthState.Idle
    }
}

