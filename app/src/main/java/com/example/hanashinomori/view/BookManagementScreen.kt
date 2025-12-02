package com.example.hanashinomori.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.hanashinomori.controller.AdminViewModel
import com.example.hanashinomori.controller.BookViewModel
import com.example.hanashinomori.model.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookManagementScreen(
    adminViewModel: AdminViewModel,
    bookViewModel: BookViewModel,
    onNavigateBack: () -> Unit
) {
    val books by bookViewModel.allBooks.collectAsState()
    val isLoading by adminViewModel.isLoading.collectAsState()
    val errorMessage by adminViewModel.errorMessage.collectAsState()
    val successMessage by adminViewModel.successMessage.collectAsState()

    var showCreateDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedBook by remember { mutableStateOf<Book?>(null) }

    LaunchedEffect(Unit) {
        bookViewModel.loadAllBooks()
    }

    LaunchedEffect(successMessage) {
        successMessage?.let {
            bookViewModel.loadAllBooks() // Recargar libros después de operación exitosa
            adminViewModel.clearMessages()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Libros") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDialog = true }) {
                Icon(Icons.Default.Add, "Crear Libro")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(books) { book ->
                        BookAdminCard(
                            book = book,
                            onEdit = {
                                selectedBook = book
                                showEditDialog = true
                            },
                            onDelete = {
                                selectedBook = book
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }

            errorMessage?.let {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(it)
                }
            }

            successMessage?.let {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(it)
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateBookDialog(
            onDismiss = { showCreateDialog = false },
            onConfirm = { title, author, category, description, coverUrl, isbn ->
                adminViewModel.createBook(title, author, category, description, coverUrl, isbn)
                showCreateDialog = false
            }
        )
    }

    if (showEditDialog && selectedBook != null) {
        EditBookDialog(
            book = selectedBook!!,
            onDismiss = { showEditDialog = false },
            onConfirm = { title, author, category, description, coverUrl, isbn ->
                adminViewModel.updateBook(
                    selectedBook!!.id!!,
                    title,
                    author,
                    category,
                    description,
                    coverUrl,
                    isbn
                )
                showEditDialog = false
            }
        )
    }

    if (showDeleteDialog && selectedBook != null) {
        DeleteConfirmDialog(
            title = "Eliminar Libro",
            message = "¿Estás seguro de eliminar '${selectedBook!!.title}'?",
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                adminViewModel.deleteBook(selectedBook!!.id!!, selectedBook!!.title)
                showDeleteDialog = false
            }
        )
    }
}

@Composable
fun BookAdminCard(
    book: Book,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = book.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, "Editar")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, "Eliminar")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBookDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String?, String?, String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Manga") }
    var description by remember { mutableStateOf("") }
    var coverUrl by remember { mutableStateOf("") }
    var isbn by remember { mutableStateOf("") }

    val categories = listOf("Manga", "Manhwa", "Donghua")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear Libro") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Autor *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Selector de categoría con chips
                Text("Categoría *", style = MaterialTheme.typography.bodyMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = coverUrl,
                    onValueChange = { coverUrl = it },
                    label = { Text("URL de Portada") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = isbn,
                    onValueChange = { isbn = it },
                    label = { Text("ISBN") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(
                    title,
                    author,
                    category,
                    description.ifBlank { null },
                    coverUrl.ifBlank { null },
                    isbn.ifBlank { null }
                )
            }) {
                Text("Crear")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBookDialog(
    book: Book,
    onDismiss: () -> Unit,
    onConfirm: (String?, String?, String?, String?, String?, String?) -> Unit
) {
    var title by remember { mutableStateOf(book.title) }
    var author by remember { mutableStateOf(book.author) }
    var category by remember { mutableStateOf(book.category) }
    var description by remember { mutableStateOf(book.description ?: "") }
    var coverUrl by remember { mutableStateOf(book.coverUrl ?: "") }
    var isbn by remember { mutableStateOf("") }

    val categories = listOf("Manga", "Manhwa", "Donghua")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Libro") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
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

                // Selector de categoría con chips
                Text("Categoría", style = MaterialTheme.typography.bodyMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = coverUrl,
                    onValueChange = { coverUrl = it },
                    label = { Text("URL de Portada") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = isbn,
                    onValueChange = { isbn = it },
                    label = { Text("ISBN (opcional)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(
                    title,
                    author,
                    category,
                    description,
                    coverUrl,
                    isbn.ifBlank { null }
                )
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

