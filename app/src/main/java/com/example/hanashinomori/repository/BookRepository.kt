package com.example.hanashinomori.repository

import android.util.Log
import com.example.hanashinomori.dto.*
import com.example.hanashinomori.model.Book
import com.example.hanashinomori.model.Favorite
import com.example.hanashinomori.network.RetrofitProvider

class BookRepository {
    private val TAG = "BookRepository"
    private val apiService = RetrofitProvider.apiService

    // ==================== BOOKS ====================

    suspend fun getAllBooks(): Result<List<Book>> {
        return try {
            Log.d(TAG, "📚 Obteniendo todos los libros...")
            val response = apiService.getAllBooks()

            if (response.isSuccessful && response.body()?.success == true) {
                val books = response.body()?.data?.map { dto ->
                    Book(
                        id = dto.id,
                        title = dto.title,
                        author = dto.author,
                        category = dto.category,
                        description = dto.description,
                        coverUrl = dto.coverUrl
                    )
                } ?: emptyList()

                Log.d(TAG, "✅ ${books.size} libros obtenidos")
                Result.success(books)
            } else {
                val errorMsg = response.body()?.message ?: "Error al obtener libros"
                Log.e(TAG, "❌ $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error de red: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun getBooksByCategory(category: String): Result<List<Book>> {
        return try {
            Log.d(TAG, "📚 Obteniendo libros de categoría: $category")
            val response = apiService.getBooksByCategory(category)

            if (response.isSuccessful && response.body()?.success == true) {
                val books = response.body()?.data?.map { dto ->
                    Book(
                        id = dto.id,
                        title = dto.title,
                        author = dto.author,
                        category = dto.category,
                        description = dto.description,
                        coverUrl = dto.coverUrl
                    )
                } ?: emptyList()

                Log.d(TAG, "✅ ${books.size} libros obtenidos de $category")
                Result.success(books)
            } else {
                val errorMsg = response.body()?.message ?: "Error al obtener libros"
                Log.e(TAG, "❌ $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error de red: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun getBookById(bookId: Long): Result<Book> {
        return try {
            Log.d(TAG, "📖 Obteniendo libro con ID: $bookId")
            val response = apiService.getBookById(bookId)

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
                    Log.d(TAG, "✅ Libro obtenido: ${book.title}")
                    Result.success(book)
                } else {
                    Result.failure(Exception("Libro no encontrado"))
                }
            } else {
                val errorMsg = response.body()?.message ?: "Error al obtener libro"
                Log.e(TAG, "❌ $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error al obtener libro: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun searchBooks(query: String): Result<List<Book>> {
        return try {
            Log.d(TAG, "🔍 Buscando libros con: '$query'")
            val response = apiService.searchBooks(query)

            if (response.isSuccessful && response.body()?.success == true) {
                val books = response.body()?.data?.map { dto ->
                    Book(
                        id = dto.id,
                        title = dto.title,
                        author = dto.author,
                        category = dto.category,
                        description = dto.description,
                        coverUrl = dto.coverUrl
                    )
                } ?: emptyList()

                Log.d(TAG, "✅ ${books.size} libros encontrados")
                Result.success(books)
            } else {
                val errorMsg = response.body()?.message ?: "Error al buscar libros"
                Log.e(TAG, "❌ $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error en búsqueda: ${e.message}")
            Result.failure(e)
        }
    }

    // ==================== FAVORITES ====================

    suspend fun getUserFavorites(userId: Long): Result<List<Favorite>> {
        return try {
            Log.d(TAG, "❤️ Obteniendo favoritos del usuario $userId")
            val response = apiService.getUserFavorites(userId)

            if (response.isSuccessful && response.body()?.success == true) {
                val favorites = response.body()?.data?.mapNotNull { dto ->
                    dto.book?.let { bookDto ->
                        Favorite(
                            id = dto.id,
                            userId = dto.userId,
                            bookId = dto.bookId,
                            book = Book(
                                id = bookDto.id,
                                title = bookDto.title,
                                author = bookDto.author,
                                category = bookDto.category,
                                description = bookDto.description,
                                coverUrl = bookDto.coverUrl
                            ),
                            isRead = dto.isRead,
                            addedAt = dto.addedAt
                        )
                    }
                } ?: emptyList()

                Log.d(TAG, "✅ ${favorites.size} favoritos obtenidos")
                Result.success(favorites)
            } else {
                val errorMsg = response.body()?.message ?: "Error al obtener favoritos"
                Log.e(TAG, "❌ $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error de red: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun addFavorite(userId: Long, bookId: Long): Result<Favorite> {
        return try {
            Log.d(TAG, "➕ Agregando libro $bookId a favoritos del usuario $userId")
            Log.d(TAG, "📤 Request: userId=$userId, bookId=$bookId")

            val request = AddFavoriteRequest(userId, bookId)
            val response = apiService.addFavorite(request)

            Log.d(TAG, "📥 Response code: ${response.code()}")
            Log.d(TAG, "📥 Response body: ${response.body()}")

            if (response.isSuccessful && response.body()?.success == true) {
                val dto = response.body()?.data

                if (dto != null) {
                    // Si el backend devuelve el objeto book completo, úsalo
                    if (dto.book != null) {
                        val favorite = Favorite(
                            id = dto.id,
                            userId = dto.userId,
                            bookId = dto.bookId,
                            book = Book(
                                id = dto.book.id,
                                title = dto.book.title,
                                author = dto.book.author,
                                category = dto.book.category,
                                description = dto.book.description,
                                coverUrl = dto.book.coverUrl
                            ),
                            isRead = dto.isRead,
                            addedAt = dto.addedAt
                        )
                        Log.d(TAG, "✅ Favorito agregado correctamente con libro completo")
                        Result.success(favorite)
                    } else {
                        // Si no viene el libro, consultarlo por separado
                        Log.d(TAG, "⚠️ Backend no devolvió el libro, consultando por separado...")
                        val bookResult = getBookById(bookId)

                        if (bookResult.isSuccess) {
                            val book = bookResult.getOrNull()!!
                            val favorite = Favorite(
                                id = dto.id,
                                userId = dto.userId,
                                bookId = dto.bookId,
                                book = book,
                                isRead = dto.isRead,
                                addedAt = dto.addedAt
                            )
                            Log.d(TAG, "✅ Favorito agregado correctamente (con consulta adicional)")
                            Result.success(favorite)
                        } else {
                            Log.e(TAG, "❌ No se pudo obtener el libro")
                            Result.failure(Exception("No se pudo obtener los datos del libro"))
                        }
                    }
                } else {
                    Log.e(TAG, "❌ Respuesta sin datos del favorito")
                    Result.failure(Exception("Respuesta sin datos"))
                }
            } else {
                val errorMsg = response.body()?.message ?: response.errorBody()?.string() ?: "Error al agregar favorito"
                Log.e(TAG, "❌ Error del servidor: $errorMsg")
                Log.e(TAG, "❌ Response code: ${response.code()}")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error de red al agregar favorito: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun removeFavorite(favoriteId: Long): Result<Boolean> {
        return try {
            Log.d(TAG, "🗑️ Eliminando favorito $favoriteId")
            val response = apiService.removeFavorite(favoriteId)

            if (response.isSuccessful && response.body()?.success == true) {
                Log.d(TAG, "✅ Favorito eliminado correctamente")
                Result.success(true)
            } else {
                val errorMsg = response.body()?.message ?: "Error al eliminar favorito"
                Log.e(TAG, "❌ $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error de red: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun toggleReadStatus(favoriteId: Long, isRead: Boolean): Result<Favorite> {
        return try {
            Log.d(TAG, "📖 Cambiando estado de lectura del favorito $favoriteId a $isRead")
            val request = ToggleReadRequest(favoriteId, isRead)
            val response = apiService.toggleReadStatus(favoriteId, request)

            if (response.isSuccessful && response.body()?.success == true) {
                val dto = response.body()?.data
                if (dto != null && dto.book != null) {
                    val favorite = Favorite(
                        id = dto.id,
                        userId = dto.userId,
                        bookId = dto.bookId,
                        book = Book(
                            id = dto.book.id,
                            title = dto.book.title,
                            author = dto.book.author,
                            category = dto.book.category,
                            description = dto.book.description,
                            coverUrl = dto.book.coverUrl
                        ),
                        isRead = dto.isRead,
                        addedAt = dto.addedAt
                    )
                    Log.d(TAG, "✅ Estado de lectura actualizado")
                    Result.success(favorite)
                } else {
                    Result.failure(Exception("Respuesta sin datos"))
                }
            } else {
                val errorMsg = response.body()?.message ?: "Error al actualizar estado"
                Log.e(TAG, "❌ $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error de red: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun checkIfFavorite(userId: Long, bookId: Long): Result<Boolean> {
        return try {
            val response = apiService.checkIfFavorite(userId, bookId)

            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data != null)
            } else {
                Result.success(false)
            }
        } catch (e: Exception) {
            Result.success(false)
        }
    }
}
