package com.example.hanashinomori.repository

import android.util.Log
import com.example.hanashinomori.dto.*
import com.example.hanashinomori.model.Book
import com.example.hanashinomori.network.RetrofitProvider

class AdminRepository(private val adminUserId: Long) {
    private val TAG = "AdminRepository"
    private val apiService = RetrofitProvider.apiService

    // ==================== USER MANAGEMENT ====================

    suspend fun getAllUsers(): Result<List<UserDto>> {
        return try {
            Log.d(TAG, "👥 Obteniendo todos los usuarios... (Admin ID: $adminUserId)")
            val response = apiService.getAllUsers(adminUserId)

            if (response.isSuccessful && response.body()?.success == true) {
                val users = response.body()?.data ?: emptyList()
                Log.d(TAG, "✅ ${users.size} usuarios obtenidos")
                Result.success(users)
            } else {
                val errorMsg = response.body()?.message ?: "Error al obtener usuarios"
                Log.e(TAG, "❌ $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error de red: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun createUser(request: CreateUserRequest): Result<UserDto> {
        return try {
            Log.d(TAG, "➕ Creando usuario: ${request.username} (Admin ID: $adminUserId)")
            // Agregar userId al request
            val requestWithUserId = request.copy(userId = adminUserId)
            val response = apiService.createUser(requestWithUserId, adminUserId)

            if (response.isSuccessful && response.body()?.success == true) {
                val user = response.body()?.data
                if (user != null) {
                    Log.d(TAG, "✅ Usuario creado: ${user.username}")
                    Result.success(user)
                } else {
                    Result.failure(Exception("Respuesta sin datos"))
                }
            } else {
                val errorMsg = response.body()?.message ?: "Error al crear usuario"
                Log.e(TAG, "❌ $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error de red: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun updateUser(userId: Long, request: UpdateUserRequest): Result<UserDto> {
        return try {
            Log.d(TAG, "✏️ Actualizando usuario ID: $userId (Admin ID: $adminUserId)")
            // Agregar userId al request
            val requestWithUserId = request.copy(userId = adminUserId)
            val response = apiService.updateUser(userId, requestWithUserId, adminUserId)

            if (response.isSuccessful && response.body()?.success == true) {
                val user = response.body()?.data
                if (user != null) {
                    Log.d(TAG, "✅ Usuario actualizado")
                    Result.success(user)
                } else {
                    Result.failure(Exception("Respuesta sin datos"))
                }
            } else {
                val errorMsg = response.body()?.message ?: "Error al actualizar usuario"
                Log.e(TAG, "❌ $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error de red: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun deleteUser(userId: Long): Result<Boolean> {
        return try {
            Log.d(TAG, "🗑️ Eliminando usuario ID: $userId (Admin ID: $adminUserId)")

            // Validar que no se intente eliminar al admin actual
            if (userId == adminUserId) {
                val errorMsg = "No puedes eliminarte a ti mismo"
                Log.e(TAG, "❌ $errorMsg")
                return Result.failure(Exception(errorMsg))
            }

            val response = apiService.deleteUser(userId, adminUserId)

            Log.d(TAG, "📥 Response code: ${response.code()}")
            Log.d(TAG, "📥 Response body: ${response.body()}")

            if (response.isSuccessful && response.body()?.success == true) {
                Log.d(TAG, "✅ Usuario eliminado exitosamente")
                Result.success(true)
            } else {
                val errorMsg = response.body()?.message ?: response.errorBody()?.string() ?: "Error al eliminar usuario"
                Log.e(TAG, "❌ Error del servidor: $errorMsg")
                Log.e(TAG, "❌ Response code: ${response.code()}")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Excepción al eliminar usuario: ${e.message}", e)
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    // ==================== BOOK MANAGEMENT ====================

    suspend fun createBook(request: CreateBookRequest): Result<Book> {
        return try {
            Log.d(TAG, "➕ Creando libro: ${request.title} (Admin ID: $adminUserId)")
            // Agregar userId al request
            val requestWithUserId = request.copy(userId = adminUserId)
            val response = apiService.adminCreateBook(requestWithUserId, adminUserId)

            if (response.isSuccessful && response.body()?.success == true) {
                val dto = response.body()?.data
                if (dto != null) {
                    val book = Book(
                        id = dto.id,
                        title = dto.title,
                        author = dto.author,
                        category = dto.category,
                        description = dto.description,
                        coverUrl = dto.coverUrl
                    )
                    Log.d(TAG, "✅ Libro creado: ${book.title}")
                    Result.success(book)
                } else {
                    Result.failure(Exception("Respuesta sin datos"))
                }
            } else {
                val errorMsg = response.body()?.message ?: "Error al crear libro"
                Log.e(TAG, "❌ $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error de red: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun updateBook(bookId: Long, request: UpdateBookRequest): Result<Book> {
        return try {
            Log.d(TAG, "✏️ Actualizando libro ID: $bookId (Admin ID: $adminUserId)")
            // Agregar userId al request
            val requestWithUserId = request.copy(userId = adminUserId)
            val response = apiService.updateBook(bookId, requestWithUserId, adminUserId)

            if (response.isSuccessful && response.body()?.success == true) {
                val dto = response.body()?.data
                if (dto != null) {
                    val book = Book(
                        id = dto.id,
                        title = dto.title,
                        author = dto.author,
                        category = dto.category,
                        description = dto.description,
                        coverUrl = dto.coverUrl
                    )
                    Log.d(TAG, "✅ Libro actualizado")
                    Result.success(book)
                } else {
                    Result.failure(Exception("Respuesta sin datos"))
                }
            } else {
                val errorMsg = response.body()?.message ?: "Error al actualizar libro"
                Log.e(TAG, "❌ $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error de red: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun deleteBook(bookId: Long): Result<Boolean> {
        return try {
            Log.d(TAG, "🗑️ Eliminando libro ID: $bookId (Admin ID: $adminUserId)")
            val response = apiService.deleteBook(bookId, adminUserId)

            if (response.isSuccessful && response.body()?.success == true) {
                Log.d(TAG, "✅ Libro eliminado")
                Result.success(true)
            } else {
                val errorMsg = response.body()?.message ?: "Error al eliminar libro"
                Log.e(TAG, "❌ $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error de red: ${e.message}")
            Result.failure(e)
        }
    }
}

