package com.example.hanashinomori.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
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
import com.example.hanashinomori.model.Favorite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    bookViewModel: BookViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToQrScanner: () -> Unit,
    onNavigateToBookDetail: (Long) -> Unit,
    scannedQrValue: String
) {
    val favoriteBooks by bookViewModel.favoriteBooks.collectAsState()
    val isLoading by bookViewModel.isLoading.collectAsState()
    val errorMessage by bookViewModel.errorMessage.collectAsState()

    var showDeleteDialog by remember { mutableStateOf<Favorite?>(null) }
    var qrResultMessage by remember { mutableStateOf<String?>(null) }
    var showQrDialog by remember { mutableStateOf(false) }

    // Procesar QR escaneado
    LaunchedEffect(scannedQrValue) {
        if (scannedQrValue.isNotEmpty()) {
            bookViewModel.addFavoriteByQr(
                qrValue = scannedQrValue,
                onSuccess = { message ->
                    qrResultMessage = message
                    showQrDialog = true
                },
                onError = { error ->
                    qrResultMessage = "❌ $error"
                    showQrDialog = true
                }
            )
        }
    }

    // Cargar favoritos al inicio
    LaunchedEffect(Unit) {
        bookViewModel.loadUserFavorites()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Biblioteca") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToQrScanner,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Escanear QR"
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                errorMessage != null -> {
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
                        Button(onClick = { bookViewModel.loadUserFavorites() }) {
                            Text("Reintentar")
                        }
                    }
                }

                favoriteBooks.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.List,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Tu biblioteca está vacía",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Agrega libros desde la pantalla principal usando el ❤️",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                "Tienes ${favoriteBooks.size} ${if (favoriteBooks.size == 1) "elemento" else "elementos"}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        items(favoriteBooks) { favorite ->
                            FavoriteItemCard(
                                favorite = favorite,
                                onToggleRead = {
                                    bookViewModel.toggleReadStatus(favorite)
                                },
                                onDelete = {
                                    showDeleteDialog = favorite
                                },
                                onClick = {
                                    onNavigateToBookDetail(favorite.bookId)
                                }
                            )
                        }
                    }
                }
            }
        }

        // Diálogo de confirmación para eliminar
        showDeleteDialog?.let { favorite ->
            AlertDialog(
                onDismissRequest = { showDeleteDialog = null },
                title = { Text("Eliminar de Favoritos") },
                text = {
                    Text("¿Estás seguro de que deseas eliminar '${favorite.book.title}' de tus favoritos?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            favorite.id?.let { bookViewModel.removeFromFavorites(it) }
                            showDeleteDialog = null
                        }
                    ) {
                        Text("Eliminar", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        // Diálogo para mostrar resultado del QR
        if (showQrDialog && qrResultMessage != null) {
            AlertDialog(
                onDismissRequest = {
                    showQrDialog = false
                    qrResultMessage = null
                },
                title = { Text("Resultado QR") },
                text = { Text(qrResultMessage ?: "") },
                confirmButton = {
                    TextButton(onClick = {
                        showQrDialog = false
                        qrResultMessage = null
                    }) {
                        Text("Aceptar")
                    }
                }
            )
        }
    }
}

@Composable
fun FavoriteItemCard(
    favorite: Favorite,
    onToggleRead: () -> Unit,
    onDelete: () -> Unit,
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
                    .size(60.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (favorite.book.category) {
                        "Manga" -> Icons.Default.Place
                        "Manhwa" -> Icons.Default.Edit
                        "Donghua" -> Icons.Default.Star
                        else -> Icons.Default.Info
                    },
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Info del libro
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = favorite.book.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = favorite.book.author,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = favorite.book.category,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (favorite.isRead) {
                        Text(
                            text = "• Leído",
                            fontSize = 12.sp,
                            color = Color.Green,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "• Por leer",
                            fontSize = 12.sp,
                            color = Color(0xFFFFA500),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Botón de marcar como leído
            IconButton(onClick = onToggleRead) {
                Icon(
                    imageVector = if (favorite.isRead)
                        Icons.Default.CheckCircle
                    else
                        Icons.Default.Check,
                    contentDescription = if (favorite.isRead)
                        "Marcar como no leído"
                    else
                        "Marcar como leído",
                    tint = if (favorite.isRead)
                        Color.Green
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Botón de eliminar
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar de favoritos",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

