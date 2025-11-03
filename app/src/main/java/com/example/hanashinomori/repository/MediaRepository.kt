package com.example.hanashinomori.repository

import com.example.hanashinomori.dao.MediaItemDao
import com.example.hanashinomori.entity.MediaItem
import kotlinx.coroutines.flow.Flow

class MediaRepository(private val mediaItemDao: MediaItemDao) {

    fun getAllMedia(): Flow<List<MediaItem>> = mediaItemDao.getAllFlow()

    fun getMyLibrary(): Flow<List<MediaItem>> = mediaItemDao.getMyLibrary()

    fun getByCategory(category: String): Flow<List<MediaItem>> = mediaItemDao.getByCategory(category)

    suspend fun updateMediaItem(mediaItem: MediaItem) {
        mediaItemDao.update(mediaItem)
    }

    suspend fun toggleLibrary(mediaItem: MediaItem) {
        val updated = mediaItem.copy(isInLibrary = !mediaItem.isInLibrary)
        mediaItemDao.update(updated)
    }

    suspend fun toggleRead(mediaItem: MediaItem) {
        val updated = mediaItem.copy(isRead = !mediaItem.isRead)
        mediaItemDao.update(updated)
    }

    suspend fun insertMedia(mediaItem: MediaItem) {
        mediaItemDao.insert(mediaItem)
    }
}

