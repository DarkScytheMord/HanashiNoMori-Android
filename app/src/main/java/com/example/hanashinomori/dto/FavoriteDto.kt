package com.example.hanashinomori.dto

data class FavoriteDto(
    val id: Long? = null,
    val userId: Long,
    val bookId: Long,
    val book: BookDto? = null,
    val isRead: Boolean = false,
    val addedAt: String? = null
)

data class FavoriteResponse(
    val success: Boolean,
    val message: String?,
    val data: FavoriteDto?
)

data class FavoritesListResponse(
    val success: Boolean,
    val message: String?,
    val data: List<FavoriteDto>?
)

data class AddFavoriteRequest(
    val userId: Long,
    val bookId: Long
)

data class ToggleReadRequest(
    val favoriteId: Long,
    val isRead: Boolean
)

