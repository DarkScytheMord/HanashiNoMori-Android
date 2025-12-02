package com.example.hanashinomori.model

data class Book(
    val id: Long? = null,
    val title: String,
    val author: String,
    val category: String,
    val description: String? = null,
    val coverUrl: String? = null
)

data class Favorite(
    val id: Long? = null,
    val userId: Long,
    val bookId: Long,
    val book: Book,
    val isRead: Boolean = false,
    val addedAt: String? = null
)

