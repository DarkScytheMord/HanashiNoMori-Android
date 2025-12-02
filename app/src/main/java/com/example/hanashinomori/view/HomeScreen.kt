package com.example.hanashinomori.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hanashinomori.controller.BookViewModel
import com.example.hanashinomori.model.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    username: String,
    isAdmin: Boolean = false,
    bookViewModel: BookViewModel,
    onNavigateToLibrary: () -> Unit,
    onNavigateToBookDetail: (Long) -> Unit,
    onNavigateToAdmin: () -> Unit = {},
    onLogout: () -> Unit
) {
    val allBooks by bookViewModel.allBooks.collectAsState()
    val favoriteBooks by bookViewModel.favoriteBooks.collectAsState()
    val isLoading by bookViewModel.isLoading.collectAsState()
    val errorMessage by bookViewModel.errorMessage.collectAsState()
    val searchQuery by bookViewModel.searchQuery.collectAsState()
    val searchResults by bookViewModel.searchResults.collectAsState()

    var selectedCategory by remember { mutableStateOf("Todos") }
    val categories = listOf("Todos", "Manga", "Manhwa", "Donghua")
    var isSearching by remember { mutableStateOf(false) }

    // Cargar libros al inicio
    LaunchedEffect(Unit) {
        bookViewModel.loadAllBooks()
    }

    // Filtrar libros por categoría
    LaunchedEffect(selectedCategory) {
        if (selectedCategory == "Todos") {
            bookViewModel.loadAllBooks()
        } else {
            bookViewModel.loadBooksByCategory(selectedCategory)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hanashi No Mori") },
                actions = {
                    // Botón de administración (solo para admins)
                    if (isAdmin) {
                        IconButton(onClick = onNavigateToAdmin) {
                            Icon(Icons.Default.Settings, "Administración")
                        }
                    }
                    IconButton(onClick = onNavigateToLibrary) {
                        Icon(Icons.Default.Favorite, "Mi Biblioteca")
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, "Cerrar Sesión")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Header con saludo
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        "¡Hola, $username!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Explora nuestra colección",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                }
            }

            // Barra de búsqueda
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    bookViewModel.updateSearchQuery(it)
                    isSearching = it.isNotEmpty()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Buscar libros por título...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            bookViewModel.clearSearch()
                            isSearching = false
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpiar")
                        }
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filtros de categoría
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        leadingIcon = if (selectedCategory == category) {
                            { Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp)) }
                        } else null
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Estado de carga
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            // Mensaje de error
            else if (errorMessage != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        errorMessage ?: "Error desconocido",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { bookViewModel.loadAllBooks() }) {
                        Text("Reintentar")
                    }
                }
            }
            // Mostrar resultados de búsqueda o lista completa
            else {
                val booksToShow = if (isSearching) searchResults else allBooks

                if (booksToShow.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                if (isSearching) "No se encontraron libros" else "No hay libros disponibles",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(booksToShow) { book ->
                            val isFavorite = bookViewModel.isBookInFavorites(book.id ?: 0)
                            BookCard(
                                book = book,
                                isFavorite = isFavorite,
                                onToggleFavorite = {
                                    if (isFavorite) {
                                        // Buscar el favorito y eliminarlo
                                        val favorite = favoriteBooks.find { it.bookId == book.id }
                                        favorite?.id?.let { bookViewModel.removeFromFavorites(it) }
                                    } else {
                                        bookViewModel.addToFavorites(book)
                                    }
                                },
                                onClick = {
                                    book.id?.let { onNavigateToBookDetail(it) }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookCard(
    book: Book,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono del libro por categoría
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(
                        when (book.category) {
                            "Manga" -> Color(0xFFE3F2FD)
                            "Manhwa" -> Color(0xFFFCE4EC)
                            "Donghua" -> Color(0xFFFFF9C4)
                            else -> Color(0xFFF5F5F5)
                        },
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (book.category) {
                        "Manga" -> Icons.Default.Place
                        "Manhwa" -> Icons.Default.Edit
                        "Donghua" -> Icons.Default.Star
                        else -> Icons.Default.Info
                    },
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = when (book.category) {
                        "Manga" -> Color(0xFF1976D2)
                        "Manhwa" -> Color(0xFFC2185B)
                        "Donghua" -> Color(0xFFF57C00)
                        else -> Color(0xFF757575)
                    }
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Info del libro
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = book.author,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = book.category,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            // Botón de favorito
            IconButton(
                onClick = onToggleFavorite
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                    tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

