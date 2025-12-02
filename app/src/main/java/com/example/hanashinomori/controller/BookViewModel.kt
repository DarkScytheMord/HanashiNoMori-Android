package com.example.hanashinomori.controller

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hanashinomori.model.Book
import com.example.hanashinomori.model.Favorite
import com.example.hanashinomori.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookViewModel(
    private val bookRepository: BookRepository = BookRepository()
) : ViewModel() {

    private val TAG = "BookViewModel"

    // Estado de los libros
    private val _allBooks = MutableStateFlow<List<Book>>(emptyList())
    val allBooks: StateFlow<List<Book>> = _allBooks.asStateFlow()

    private val _favoriteBooks = MutableStateFlow<List<Favorite>>(emptyList())
    val favoriteBooks: StateFlow<List<Favorite>> = _favoriteBooks.asStateFlow()

    // Estado para libro seleccionado (detalles)
    private val _selectedBook = MutableStateFlow<Book?>(null)
    val selectedBook: StateFlow<Book?> = _selectedBook.asStateFlow()

    // Estado de búsqueda
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Book>>(emptyList())
    val searchResults: StateFlow<List<Book>> = _searchResults.asStateFlow()

    // Estados de carga y error
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // ID del usuario actual
    private var currentUserId: Long? = null

    fun setUserId(userId: Long) {
        currentUserId = userId
        Log.d(TAG, "📝 Usuario configurado: $userId")
        loadUserFavorites()
    }

    // ==================== CARGAR LIBROS ====================

    fun loadAllBooks() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            bookRepository.getAllBooks()
                .onSuccess { books ->
                    _allBooks.value = books
                    Log.d(TAG, "✅ ${books.size} libros cargados")
                }
                .onFailure { error ->
                    _errorMessage.value = error.message
                    Log.e(TAG, "❌ Error cargando libros: ${error.message}")
                }

            _isLoading.value = false
        }
    }

    fun loadBooksByCategory(category: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            bookRepository.getBooksByCategory(category)
                .onSuccess { books ->
                    _allBooks.value = books
                    Log.d(TAG, "✅ ${books.size} libros de $category cargados")
                }
                .onFailure { error ->
                    _errorMessage.value = error.message
                    Log.e(TAG, "❌ Error cargando libros: ${error.message}")
                }

            _isLoading.value = false
        }
    }

    // ==================== FAVORITOS ====================

    fun loadUserFavorites() {
        val userId = currentUserId
        if (userId == null) {
            Log.w(TAG, "⚠️ No hay usuario configurado")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            bookRepository.getUserFavorites(userId)
                .onSuccess { favorites ->
                    _favoriteBooks.value = favorites
                    Log.d(TAG, "✅ ${favorites.size} favoritos cargados")
                }
                .onFailure { error ->
                    _errorMessage.value = error.message
                    Log.e(TAG, "❌ Error cargando favoritos: ${error.message}")
                }

            _isLoading.value = false
        }
    }

    fun addToFavorites(book: Book) {
        val userId = currentUserId
        if (userId == null) {
            _errorMessage.value = "Usuario no identificado"
            return
        }

        val bookId = book.id
        if (bookId == null) {
            _errorMessage.value = "Libro sin ID"
            return
        }

        viewModelScope.launch {
            bookRepository.addFavorite(userId, bookId)
                .onSuccess { favorite ->
                    // Recargar favoritos
                    loadUserFavorites()
                    Log.d(TAG, "✅ Libro agregado a favoritos")
                }
                .onFailure { error ->
                    _errorMessage.value = error.message
                    Log.e(TAG, "❌ Error agregando favorito: ${error.message}")
                }
        }
    }

    fun removeFromFavorites(favoriteId: Long) {
        viewModelScope.launch {
            bookRepository.removeFavorite(favoriteId)
                .onSuccess {
                    // Recargar favoritos
                    loadUserFavorites()
                    Log.d(TAG, "✅ Libro eliminado de favoritos")
                }
                .onFailure { error ->
                    _errorMessage.value = error.message
                    Log.e(TAG, "❌ Error eliminando favorito: ${error.message}")
                }
        }
    }

    fun toggleReadStatus(favorite: Favorite) {
        val favoriteId = favorite.id
        if (favoriteId == null) {
            _errorMessage.value = "Favorito sin ID"
            return
        }

        viewModelScope.launch {
            bookRepository.toggleReadStatus(favoriteId, !favorite.isRead)
                .onSuccess {
                    // Recargar favoritos
                    loadUserFavorites()
                    Log.d(TAG, "✅ Estado de lectura actualizado")
                }
                .onFailure { error ->
                    _errorMessage.value = error.message
                    Log.e(TAG, "❌ Error actualizando estado: ${error.message}")
                }
        }
    }

    fun isBookInFavorites(bookId: Long): Boolean {
        return _favoriteBooks.value.any { it.bookId == bookId }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    // ==================== BÚSQUEDA ====================

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.isEmpty()) {
            _searchResults.value = emptyList()
        } else {
            searchBooks(query)
        }
    }

    private fun searchBooks(query: String) {
        viewModelScope.launch {
            bookRepository.searchBooks(query)
                .onSuccess { books ->
                    _searchResults.value = books
                    Log.d(TAG, "🔍 ${books.size} libros encontrados para '$query'")
                }
                .onFailure { error ->
                    Log.e(TAG, "❌ Error en búsqueda: ${error.message}")
                    _searchResults.value = emptyList()
                }
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchResults.value = emptyList()
    }

    // ==================== DETALLES DE LIBRO ====================

    fun loadBookDetails(bookId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            bookRepository.getBookById(bookId)
                .onSuccess { book ->
                    _selectedBook.value = book
                    Log.d(TAG, "📖 Detalles del libro cargados: ${book.title}")
                }
                .onFailure { error ->
                    _errorMessage.value = error.message
                    Log.e(TAG, "❌ Error cargando detalles: ${error.message}")
                }

            _isLoading.value = false
        }
    }

    fun clearSelectedBook() {
        _selectedBook.value = null
    }

    // ==================== QR SCANNER ====================

    fun addFavoriteByQr(qrValue: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Parsear el QR (esperamos formato: "BOOK:123")
                if (qrValue.startsWith("BOOK:", ignoreCase = true)) {
                    val bookId = qrValue.substringAfter(":").toLongOrNull()

                    if (bookId == null) {
                        onError("QR inválido: formato incorrecto")
                        return@launch
                    }

                    val userId = currentUserId
                    if (userId == null) {
                        onError("No hay usuario logueado")
                        return@launch
                    }

                    Log.d(TAG, "📷 QR detectado - Libro ID: $bookId")

                    // Primero obtener detalles del libro
                    bookRepository.getBookById(bookId)
                        .onSuccess { book ->
                            // Luego agregarlo a favoritos
                            bookRepository.addFavorite(userId, bookId)
                                .onSuccess {
                                    loadUserFavorites()
                                    onSuccess("✅ '${book.title}' agregado a favoritos")
                                }
                                .onFailure { error ->
                                    onError("Error al agregar: ${error.message}")
                                }
                        }
                        .onFailure { error ->
                            onError("Libro no encontrado: ${error.message}")
                        }
                } else {
                    onError("QR no válido. Debe ser formato: BOOK:ID")
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error procesando QR: ${e.message}")
                onError("Error: ${e.message}")
            }
        }
    }
}
