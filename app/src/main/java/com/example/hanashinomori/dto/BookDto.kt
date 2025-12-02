package com.example.hanashinomori.dto

import com.google.gson.annotations.SerializedName

data class BookDto(
    val id: Long? = null,
    val title: String,
    val author: String,
    val category: String,
    val description: String? = null,

    @SerializedName("coverUrl")
    val coverUrl: String? = null,

    val isbn: String? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null
)

data class BookResponse(
    val success: Boolean,
    val message: String?,
    val data: BookDto?
)

data class BooksListResponse(
    val success: Boolean,
    val message: String?,
    val data: List<BookDto>?
)

