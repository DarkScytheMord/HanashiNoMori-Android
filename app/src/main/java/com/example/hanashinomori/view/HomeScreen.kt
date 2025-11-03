package com.example.hanashinomori.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
import com.example.hanashinomori.controller.MediaViewModel
import com.example.hanashinomori.entity.MediaItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    username: String,
    mediaViewModel: MediaViewModel,
    onNavigateToLibrary: () -> Unit,
    onLogout: () -> Unit
) {
    val allMedia by mediaViewModel.allMedia.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    val libros = remember(allMedia) { allMedia.filter { it.category == "Libro" } }
    val mangas = remember(allMedia) { allMedia.filter { it.category == "Manga" } }
    val manhwas = remember(allMedia) { allMedia.filter { it.category == "Manhwa" } }
    val donghuas = remember(allMedia) { allMedia.filter { it.category == "Donghua" } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("HanashiNoMori", fontWeight = FontWeight.Bold)
                        Text(
                            "Hola, $username",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToLibrary) {
                        Icon(Icons.AutoMirrored.Filled.List, "Mi Biblioteca")
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, "Cerrar sesión")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true }
            ) {
                Icon(Icons.Default.Add, "Agregar elemento")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        "Bienvenido a tu biblioteca digital",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Explora nuestra colección de libros, mangas, manhwas y donghuas",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                CategorySection(
                    title = "📚 Libros",
                    items = libros,
                    onToggleLibrary = { item -> mediaViewModel.toggleLibrary(item) }
                )
            }

            item {
                CategorySection(
                    title = "🎌 Mangas",
                    items = mangas,
                    onToggleLibrary = { item -> mediaViewModel.toggleLibrary(item) }
                )
            }

            item {
                CategorySection(
                    title = "🇰🇷 Manhwas",
                    items = manhwas,
                    onToggleLibrary = { item -> mediaViewModel.toggleLibrary(item) }
                )
            }

            item {
                CategorySection(
                    title = "🇨🇳 Donghuas",
                    items = donghuas,
                    onToggleLibrary = { item -> mediaViewModel.toggleLibrary(item) }
                )
            }
        }

        if (showAddDialog) {
            AddMediaDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { title, author, category ->
                    mediaViewModel.addCustomMedia(title, author, category)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun CategorySection(
    title: String,
    items: List<MediaItem>,
    onToggleLibrary: (MediaItem) -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (items.isEmpty()) {
            Text(
                text = "No hay elementos en esta categoría",
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items) { item ->
                    MediaCard(
                        mediaItem = item,
                        onToggleLibrary = { onToggleLibrary(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun MediaCard(
    mediaItem: MediaItem,
    onToggleLibrary: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(220.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.medium
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (mediaItem.category) {
                            "Manga" -> Icons.Default.Place
                            "Manhwa" -> Icons.Default.Edit
                            "Donghua" -> Icons.Default.Star
                            else -> Icons.Default.Info
                        },
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = mediaItem.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 2
                )

                Text(
                    text = mediaItem.author,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }

            IconButton(
                onClick = onToggleLibrary,
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = if (mediaItem.isInLibrary)
                        Icons.Default.Favorite
                    else
                        Icons.Default.FavoriteBorder,
                    contentDescription = if (mediaItem.isInLibrary)
                        "Quitar de biblioteca"
                    else
                        "Agregar a biblioteca",
                    tint = if (mediaItem.isInLibrary)
                        Color.Red
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun AddMediaDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, author: String, category: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Libro") }
    var expanded by remember { mutableStateOf(false) }

    val categories = listOf("Libro", "Manga", "Manhwa", "Donghua")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar a mi biblioteca") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Autor") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Box {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = { },
                        label = { Text("Categoría") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(
                                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Expandir"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    selectedCategory = category
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && author.isNotBlank()) {
                        onConfirm(title, author, selectedCategory)
                    }
                },
                enabled = title.isNotBlank() && author.isNotBlank()
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
