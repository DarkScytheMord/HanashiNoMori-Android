package com.example.hanashinomori.controller

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hanashinomori.dto.*
import com.example.hanashinomori.model.Book
import com.example.hanashinomori.repository.AdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminViewModel(
    private val adminUserId: Long
) : ViewModel() {

    private val TAG = "AdminViewModel"
    private val adminRepository = AdminRepository(adminUserId)

    // Estado de usuarios
    private val _users = MutableStateFlow<List<UserDto>>(emptyList())
    val users: StateFlow<List<UserDto>> = _users.asStateFlow()

    // Estado de libros (para admin)
    private val _adminBooks = MutableStateFlow<List<Book>>(emptyList())
    val adminBooks: StateFlow<List<Book>> = _adminBooks.asStateFlow()

    // Estados de carga y error
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    // ==================== USER MANAGEMENT ====================

    fun loadAllUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            adminRepository.getAllUsers()
                .onSuccess { users ->
                    _users.value = users
                    Log.d(TAG, "✅ ${users.size} usuarios cargados")
                }
                .onFailure { error ->
                    _errorMessage.value = error.message
                    Log.e(TAG, "❌ Error cargando usuarios: ${error.message}")
                }

            _isLoading.value = false
        }
    }

    fun createUser(username: String, email: String, password: String, isAdmin: Boolean) {
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            _errorMessage.value = "Todos los campos son obligatorios"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val request = CreateUserRequest(
                username = username,
                email = email,
                password = password,
                isAdmin = isAdmin
            )

            adminRepository.createUser(request)
                .onSuccess { user ->
                    _successMessage.value = "Usuario '${user.username}' creado exitosamente"
                    loadAllUsers() // Recargar lista
                }
                .onFailure { error ->
                    _errorMessage.value = error.message
                    Log.e(TAG, "❌ Error creando usuario: ${error.message}")
                }

            _isLoading.value = false
        }
    }

    fun updateUser(userId: Long, username: String?, email: String?, password: String?, isAdmin: Boolean?) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val request = UpdateUserRequest(
                username = username,
                email = email,
                password = password,
                isAdmin = isAdmin
            )

            adminRepository.updateUser(userId, request)
                .onSuccess { user ->
                    _successMessage.value = "Usuario '${user.username}' actualizado"
                    loadAllUsers() // Recargar lista
                }
                .onFailure { error ->
                    _errorMessage.value = error.message
                    Log.e(TAG, "❌ Error actualizando usuario: ${error.message}")
                }

            _isLoading.value = false
        }
    }

    fun deleteUser(userId: Long, username: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            Log.d(TAG, "🔄 Intentando eliminar usuario: $username (ID: $userId)")

            adminRepository.deleteUser(userId)
                .onSuccess {
                    _successMessage.value = "Usuario '$username' eliminado correctamente"
                    Log.d(TAG, "✅ Usuario '$username' eliminado, recargando lista...")
                    loadAllUsers() // Recargar lista
                }
                .onFailure { error ->
                    val errorMsg = error.message ?: "Error desconocido al eliminar usuario"
                    _errorMessage.value = "Error al eliminar '$username': $errorMsg"
                    Log.e(TAG, "❌ Error eliminando usuario '$username': $errorMsg", error)
                }

            _isLoading.value = false
        }
    }

    // ==================== BOOK MANAGEMENT ====================

    fun createBook(
        title: String,
        author: String,
        category: String,
        description: String?,
        coverUrl: String?,
        isbn: String?
    ) {
        if (title.isBlank() || author.isBlank() || category.isBlank()) {
            _errorMessage.value = "Título, autor y categoría son obligatorios"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val request = CreateBookRequest(
                title = title,
                author = author,
                category = category,
                description = description,
                coverUrl = coverUrl,
                isbn = isbn
            )

            adminRepository.createBook(request)
                .onSuccess { book ->
                    _successMessage.value = "Libro '${book.title}' creado exitosamente"
                }
                .onFailure { error ->
                    _errorMessage.value = error.message
                    Log.e(TAG, "❌ Error creando libro: ${error.message}")
                }

            _isLoading.value = false
        }
    }

    fun updateBook(
        bookId: Long,
        title: String?,
        author: String?,
        category: String?,
        description: String?,
        coverUrl: String?,
        isbn: String?
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val request = UpdateBookRequest(
                title = title,
                author = author,
                category = category,
                description = description,
                coverUrl = coverUrl,
                isbn = isbn
            )

            adminRepository.updateBook(bookId, request)
                .onSuccess { book ->
                    _successMessage.value = "Libro '${book.title}' actualizado"
                }
                .onFailure { error ->
                    _errorMessage.value = error.message
                    Log.e(TAG, "❌ Error actualizando libro: ${error.message}")
                }

            _isLoading.value = false
        }
    }

    fun deleteBook(bookId: Long, title: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            adminRepository.deleteBook(bookId)
                .onSuccess {
                    _successMessage.value = "Libro '$title' eliminado"
                }
                .onFailure { error ->
                    _errorMessage.value = error.message
                    Log.e(TAG, "❌ Error eliminando libro: ${error.message}")
                }

            _isLoading.value = false
        }
    }

    // ==================== UTILIDADES ====================

    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }
}

