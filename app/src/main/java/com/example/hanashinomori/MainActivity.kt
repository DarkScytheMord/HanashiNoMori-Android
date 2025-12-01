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
import com.example.hanashinomori.controller.MediaViewModel
import com.example.hanashinomori.db.AppDatabase
import com.example.hanashinomori.repository.AuthRepository
import com.example.hanashinomori.repository.MediaRepository
import com.example.hanashinomori.view.HomeScreen
import com.example.hanashinomori.view.LibraryScreen
import com.example.hanashinomori.view.LoginScreen
import com.example.hanashinomori.view.RegisterScreen
import com.example.hanashinomori.view.QrScannerScreen

class MainActivity : ComponentActivity() {

    private lateinit var  authViewModel: AuthViewModel
    private lateinit var mediaViewModel: MediaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Database
        val database = AppDatabase.getDatabase(applicationContext)

        // Inicializar Repositories
        val authRepository = AuthRepository(database.userDao())
        val mediaRepository = MediaRepository(database.mediaItemDao())

        // Inicializar ViewModels
        authViewModel = AuthViewModel(authRepository)
        mediaViewModel = MediaViewModel(mediaRepository)

        setContent {
            MaterialTheme {
                AppNavigation(authViewModel, mediaViewModel)
            }
        }
    }
}

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    mediaViewModel: MediaViewModel
) {
    val navController = rememberNavController()
    var currentUsername by remember { mutableStateOf("") }
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
                onLoginSuccess = { username ->
                    currentUsername = username
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
                username = currentUsername,
                mediaViewModel = mediaViewModel,
                onNavigateToLibrary = {
                    navController.navigate("library")
                },
                onLogout = {
                    authViewModel.logout()
                    currentUsername = ""
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("library") {
            LibraryScreen(
                mediaViewModel = mediaViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToQrScanner = {
                    navController.navigate("qr_scanner")
                },
                scannedQrValue = scannedQrValue
            )
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

