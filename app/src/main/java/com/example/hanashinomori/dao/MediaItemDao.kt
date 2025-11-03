package com.example.hanashinomori.dao

import androidx.room.*
import com.example.hanashinomori.entity.MediaItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaItemDao {

    @Query("SELECT * FROM media_items")
    fun getAllFlow(): Flow<List<MediaItem>>

    @Query("SELECT * FROM media_items WHERE isInLibrary = 1")
    fun getMyLibrary(): Flow<List<MediaItem>>

    @Query("SELECT * FROM media_items WHERE category = :category")
    fun getByCategory(category: String): Flow<List<MediaItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mediaItem: MediaItem)

    @Update
    suspend fun update(mediaItem: MediaItem)

    @Query("DELETE FROM media_items")
    suspend fun deleteAll()
}

