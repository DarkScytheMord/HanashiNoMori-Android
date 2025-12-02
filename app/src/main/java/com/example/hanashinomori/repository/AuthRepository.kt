package com.example.hanashinomori.repository

import android.util.Log
import com.example.hanashinomori.dto.LoginRequest
import com.example.hanashinomori.dto.RegisterRequest
import com.example.hanashinomori.network.RetrofitProvider

data class UserSession(
    val userId: Long,
    val username: String,
    val email: String,
    val token: String? = null,
    val isAdmin: Boolean = false
)

class AuthRepository {
    private val TAG = "AuthRepository"
    private val apiService = RetrofitProvider.apiService

    // Session management (in-memory)
    private var currentSession: UserSession? = null

    fun getCurrentSession(): UserSession? = currentSession

    fun isLoggedIn(): Boolean = currentSession != null

    fun logout() {
        currentSession = null
        Log.d(TAG, "🚪 Sesión cerrada")
    }

    /**
     * Registro usando BACKEND
     */
    suspend fun register(username: String, email: String, password: String): Result<UserSession> {
        return try {
            Log.d(TAG, "=== INTENTANDO REGISTRO ===")
            Log.d(TAG, "Username: $username")
            Log.d(TAG, "Email: $email")

            val request = RegisterRequest(
                username = username,
                email = email,
                password = password
            )

            Log.d(TAG, "Enviando a backend...")
            val response = apiService.register(request)

            Log.d(TAG, "Response code: ${response.code()}")

            if (response.isSuccessful && response.body()?.success == true) {
                val body = response.body()!!

                Log.d(TAG, "📦 Response body completo: $body")
                Log.d(TAG, "📦 body.data: ${body.data}")
                Log.d(TAG, "📦 body.data?.userId: ${body.data?.userId}")
                Log.d(TAG, "📦 body.userId: ${body.userId}")

                val userId = body.data?.userId ?: body.userId ?: 0L
                val responseUsername = body.data?.username ?: body.username ?: username
                val responseEmail = body.data?.email ?: body.email ?: email
                val token = body.data?.token ?: body.token
                val isAdmin = body.data?.isAdmin ?: false

                Log.d(TAG, "✅ REGISTRO EXITOSO - userId: $userId, isAdmin: $isAdmin")

                // Validar que userId no sea 0
                if (userId == 0L) {
                    Log.e(TAG, "❌ ERROR CRÍTICO: userId es 0 - El backend no devolvió userId válido")
                    return Result.failure(Exception("Error: No se pudo obtener ID de usuario del servidor"))
                }

                val session = UserSession(
                    userId = userId,
                    username = responseUsername,
                    email = responseEmail,
                    token = token,
                    isAdmin = isAdmin
                )

                // Guardar sesión
                currentSession = session

                Result.success(session)
            } else {
                val errorMsg = response.body()?.message ?: "Error en el registro"
                Log.e(TAG, "❌ ERROR: $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ EXCEPCIÓN en register: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    /**
     * Login usando BACKEND
     */
    suspend fun login(usernameOrEmail: String, password: String): Result<UserSession> {
        return try {
            Log.d(TAG, "=== INTENTANDO LOGIN ===")
            Log.d(TAG, "UsernameOrEmail: $usernameOrEmail")

            val request = LoginRequest(
                usernameOrEmail = usernameOrEmail,
                password = password
            )

            Log.d(TAG, "Enviando a backend...")
            val response = apiService.login(request)

            Log.d(TAG, "Response code: ${response.code()}")

            if (response.isSuccessful && response.body()?.success == true) {
                val body = response.body()!!

                Log.d(TAG, "📦 Response body completo: $body")
                Log.d(TAG, "📦 body.data: ${body.data}")
                Log.d(TAG, "📦 body.data?.userId: ${body.data?.userId}")
                Log.d(TAG, "📦 body.userId: ${body.userId}")

                val userId = body.data?.userId ?: body.userId ?: 0L
                val username = body.data?.username ?: body.username ?: usernameOrEmail
                val email = body.data?.email ?: body.email ?: ""
                val token = body.data?.token ?: body.token
                val isAdmin = body.data?.isAdmin ?: false

                Log.d(TAG, "✅ LOGIN EXITOSO - userId: $userId, isAdmin: $isAdmin")

                // Validar que userId no sea 0
                if (userId == 0L) {
                    Log.e(TAG, "❌ ERROR CRÍTICO: userId es 0 - El backend no devolvió userId válido")
                    return Result.failure(Exception("Error: No se pudo obtener ID de usuario del servidor"))
                }

                val session = UserSession(
                    userId = userId,
                    username = username,
                    email = email,
                    token = token,
                    isAdmin = isAdmin
                )

                // Guardar sesión
                currentSession = session

                Result.success(session)
            } else {
                val errorMsg = response.body()?.message ?: "Credenciales incorrectas"
                Log.e(TAG, "❌ ERROR: $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ EXCEPCIÓN en login: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
}


