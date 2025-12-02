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
                bookViewModel = bookViewModel,
                onNavigateToLibrary = {
                    navController.navigate("library")
                },
                onNavigateToBookDetail = { bookId ->
                    navController.navigate("book_detail/$bookId")
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
                scannedQrValue = scannedQrValue
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
            QrScannerScreen(
                onQrScanned = { qrValue ->
                    scannedQrValue = qrValue
                    navController.popBackStack()
                },
                onClose = {
                    navController.popBackStack()
                }
            )
        }
    }
}

