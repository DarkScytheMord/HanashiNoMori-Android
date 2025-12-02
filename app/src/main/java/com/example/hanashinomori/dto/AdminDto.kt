package com.example.hanashinomori.dto

// ==================== USER MANAGEMENT ====================

data class UserDto(
    val userId: Long,
    val username: String,
    val email: String,
    val isAdmin: Boolean,
    val createdAt: String? = null
)

data class UsersListResponse(
    val success: Boolean,
    val message: String?,
    val data: List<UserDto>?
)

data class UserResponse(
    val success: Boolean,
    val message: String?,
    val data: UserDto?
)

data class CreateUserRequest(
    val username: String,
    val email: String,
    val password: String,
    val isAdmin: Boolean = false,
    val userId: Long? = null  // ID del admin que crea el usuario
)

data class UpdateUserRequest(
    val username: String? = null,
    val email: String? = null,
    val password: String? = null,
    val isAdmin: Boolean? = null,
    val userId: Long? = null  // ID del admin que actualiza
)

// ==================== BOOK MANAGEMENT ====================
// Request DTOs
data class CreateBookRequest(
    val title: String,
    val author: String,
    val category: String,
    val description: String? = null,
    val coverUrl: String? = null,
    val isbn: String? = null,
    val userId: Long? = null  // ID del admin que crea el libro
)

data class UpdateBookRequest(
    val title: String? = null,
    val author: String? = null,
    val category: String? = null,
    val description: String? = null,
    val coverUrl: String? = null,
    val isbn: String? = null,
    val userId: Long? = null  // ID del admin que actualiza
)

data class DeleteResponse(
    val success: Boolean,
    val message: String?,
    val data: Any?
)

