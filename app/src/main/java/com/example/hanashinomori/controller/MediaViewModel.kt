package com.example.hanashinomori.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hanashinomori.entity.MediaItem
import com.example.hanashinomori.repository.MediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MediaViewModel(private val mediaRepository: MediaRepository) : ViewModel() {

    private val _allMedia = MutableStateFlow<List<MediaItem>>(emptyList())
    val allMedia: StateFlow<List<MediaItem>> = _allMedia

    private val _myLibrary = MutableStateFlow<List<MediaItem>>(emptyList())
    val myLibrary: StateFlow<List<MediaItem>> = _myLibrary

    init {
        loadAllMedia()
        loadMyLibrary()
    }

    private fun loadAllMedia() {
        viewModelScope.launch {
            mediaRepository.getAllMedia().collect { items ->
                _allMedia.value = items
            }
        }
    }

    private fun loadMyLibrary() {
        viewModelScope.launch {
            mediaRepository.getMyLibrary().collect { items ->
                _myLibrary.value = items
            }
        }
    }

    fun toggleLibrary(mediaItem: MediaItem) {
        viewModelScope.launch {
            mediaRepository.toggleLibrary(mediaItem)
        }
    }

    fun toggleRead(mediaItem: MediaItem) {
        viewModelScope.launch {
            mediaRepository.toggleRead(mediaItem)
        }
    }

    fun getBooksByCategory(category: String): List<MediaItem> {
        return _allMedia.value.filter { it.category == category }
    }

    fun addCustomMedia(title: String, author: String, category: String) {
        viewModelScope.launch {
            val newItem = MediaItem(
                title = title,
                author = author,
                category = category,
                isInLibrary = true, // Agregar directamente a la biblioteca
                isRead = false
            )
            mediaRepository.insertMedia(newItem)
        }
    }
}

