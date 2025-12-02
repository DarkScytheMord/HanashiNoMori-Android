package com.example.hanashinomori.api

import com.example.hanashinomori.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // AUTH ENDPOINTS
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterDto): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginDto): Response<AuthResponse>

    // BOOKS ENDPOINTS
    @GET("api/books")
    suspend fun getAllBooks(): Response<BooksListResponse>

    @GET("api/books/{id}")
    suspend fun getBookById(@Path("id") id: Long): Response<BookResponse>

    @GET("api/books/category/{category}")
    suspend fun getBooksByCategory(@Path("category") category: String): Response<BooksListResponse>

    @GET("api/books/search")
    suspend fun searchBooks(@Query("title") title: String): Response<BooksListResponse>

    @POST("api/books")
    suspend fun createBook(@Body book: BookDto): Response<BookResponse>

    // FAVORITES ENDPOINTS
    @GET("api/favorites/user/{userId}")
    suspend fun getUserFavorites(@Path("userId") userId: Long): Response<FavoritesListResponse>

    @POST("api/favorites")
    suspend fun addFavorite(@Body request: AddFavoriteRequest): Response<FavoriteResponse>

    @DELETE("api/favorites/{favoriteId}")
    suspend fun removeFavorite(@Path("favoriteId") favoriteId: Long): Response<FavoriteResponse>

    @PUT("api/favorites/{favoriteId}/toggle-read")
    suspend fun toggleReadStatus(
        @Path("favoriteId") favoriteId: Long,
        @Body request: ToggleReadRequest
    ): Response<FavoriteResponse>

    @GET("api/favorites/check/{userId}/{bookId}")
    suspend fun checkIfFavorite(
        @Path("userId") userId: Long,
        @Path("bookId") bookId: Long
    ): Response<FavoriteResponse>
}

