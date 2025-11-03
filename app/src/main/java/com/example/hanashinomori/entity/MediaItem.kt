package com.example.hanashinomori.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_items")
data class MediaItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val author: String,
    val category: String, // "Libro", "Manga", "Manhwa", "Donghua"
    val imageUrl: String = "",
    var isInLibrary: Boolean = false,
    var isRead: Boolean = false
)

