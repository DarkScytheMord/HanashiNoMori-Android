package com.example.hanashinomori

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hanashinomori.controller.AuthViewModel
import com.example.hanashinomori.controller.BookViewModel
import com.example.hanashinomori.view.BookDetailScreen
import com.example.hanashinomori.view.HomeScreen
import com.example.hanashinomori.view.LibraryScreen
import com.example.hanashinomori.view.LoginScreen
import com.example.hanashinomori.view.RegisterScreen
import com.example.hanashinomori.view.QrScannerScreen

class MainActivity : ComponentActivity() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var bookViewModel: BookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar ViewModels (sin base de datos local)
        authViewModel = AuthViewModel()
        bookViewModel = BookViewModel()

        setContent {
            MaterialTheme {
                AppNavigation(authViewModel, bookViewModel)
            }
        }
    }
}

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    bookViewModel: BookViewModel
) {
    val navController = rememberNavController()
    val currentUser by authViewModel.currentUser.collectAsState()
    var scannedQrValue by remember { mutableStateOf("") }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onLoginSuccess = { userId ->
                    // Configurar userId en bookViewModel
                    bookViewModel.setUserId(userId)
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                authViewModel = authViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.popBackStack()
                }
            )
        }

        composable("home") {
            HomeScreen(
                username = currentUser?.username ?: "",
                isAdmin = currentUser?.isAdmin ?: false,
                bookViewModel = bookViewModel,
                onNavigateToLibrary = {
                    navController.navigate("library")
                },
                onNavigateToBookDetail = { bookId ->
                    navController.navigate("book_detail/$bookId")
                },
                onNavigateToAdmin = {
                    navController.navigate("admin_dashboard")
                },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("library") {
            LibraryScreen(
                bookViewModel = bookViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToQrScanner = {
                    navController.navigate("qr_scanner")
                },
                onNavigateToBookDetail = { bookId ->
                    navController.navigate("book_detail/$bookId")
                },
                scannedQrValue = scannedQrValue,
                onQrValueProcessed = {
                    // 🔧 LIMPIAR el valor del QR después de procesarlo
                    scannedQrValue = ""
                }
            )
        }

        composable("book_detail/{bookId}") { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId")?.toLongOrNull()
            if (bookId != null) {
                BookDetailScreen(
                    bookId = bookId,
                    viewModel = bookViewModel,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable("qr_scanner") {
            // 🔧 CORRECCIÓN: Limpiar el valor del QR al entrar al escáner
            LaunchedEffect(Unit) {
                scannedQrValue = ""
            }

            QrScannerScreen(
                onQrScanned = { qrValue ->
                    // Solo procesar si no está vacío y es diferente al anterior
                    if (qrValue.isNotBlank() && qrValue != scannedQrValue) {
                        scannedQrValue = qrValue
                        navController.popBackStack()
                    }
                },
                onClose = {
                    navController.popBackStack()
                }
            )
        }

        // ADMIN ROUTES
        composable("admin_dashboard") {
            com.example.hanashinomori.view.AdminDashboardScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToUserManagement = {
                    navController.navigate("admin_users")
                },
                onNavigateToBookManagement = {
                    navController.navigate("admin_books")
                }
            )
        }

        composable("admin_users") {
            val userId = currentUser?.userId ?: 0L
            android.util.Log.d("MainActivity", "🔑 Creando AdminViewModel con userId: $userId")
            android.util.Log.d("MainActivity", "👤 currentUser: ${currentUser?.username}, isAdmin: ${currentUser?.isAdmin}")

            val adminViewModel = remember(userId) {
                com.example.hanashinomori.controller.AdminViewModel(userId)
            }
            com.example.hanashinomori.view.UserManagementScreen(
                adminViewModel = adminViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("admin_books") {
            val userId = currentUser?.userId ?: 0L
            android.util.Log.d("MainActivity", "🔑 Creando AdminViewModel con userId: $userId")
            android.util.Log.d("MainActivity", "👤 currentUser: ${currentUser?.username}, isAdmin: ${currentUser?.isAdmin}")

            val adminViewModel = remember(userId) {
                com.example.hanashinomori.controller.AdminViewModel(userId)
            }
            com.example.hanashinomori.view.BookManagementScreen(
                adminViewModel = adminViewModel,
                bookViewModel = bookViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

