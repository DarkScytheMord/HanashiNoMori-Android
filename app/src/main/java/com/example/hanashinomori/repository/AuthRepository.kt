package com.example.hanashinomori.repository

import android.util.Log
import com.example.hanashinomori.api.ApiService
import com.example.hanashinomori.dao.UserDao
import com.example.hanashinomori.dto.LoginRequest
import com.example.hanashinomori.dto.RegisterRequest
import com.example.hanashinomori.entity.User
import com.example.hanashinomori.network.RetrofitProvider

class AuthRepository(private val userDao: UserDao) {

    private val apiService: ApiService = RetrofitProvider.apiService

    /**
     * Registro usando BACKEND (Spring Boot)
     */
    suspend fun register(username: String, email: String, password: String): Result<User> {
        return try {
            Log.d("AuthRepository", "=== INTENTANDO REGISTRO ===")
            Log.d("AuthRepository", "Username: $username")
            Log.d("AuthRepository", "Email: $email")
            
            // Crear request
            val request = RegisterRequest(
                username = username,
                email = email,
                password = password
            )
            
            Log.d("AuthRepository", "Request creado: $request")
            Log.d("AuthRepository", "Enviando a backend...")
            
            // Llamar al backend
            val response = apiService.register(request)
            
            Log.d("AuthRepository", "Response code: ${response.code()}")

            if (response.isSuccessful && response.body()?.success == true) {
                val registerResponse = response.body()!!

                // Obtener userId desde data o desde el campo directo
                val userId = registerResponse.data?.userId ?: registerResponse.userId ?: 0
                val responseUsername = registerResponse.data?.username ?: registerResponse.username ?: username
                val responseEmail = registerResponse.data?.email ?: registerResponse.email ?: email

                Log.d("AuthRepository", "✅ REGISTRO EXITOSO - userId: $userId")

                // Crear user local (opcional, para cache)
                val user = User(
                    id = userId,
                    username = responseUsername,
                    email = responseEmail,
                    password = password
                )
                
                // Guardar en BD local como cache
                try {
                    userDao.insert(user)
                    Log.d("AuthRepository", "Usuario guardado en BD local como cache")
                } catch (e: Exception) {
                    Log.w("AuthRepository", "No se pudo guardar en BD local: ${e.message}")
                }
                
                Result.success(user)
            } else {
                // Leer el mensaje de error del backend
                val errorMsg = if (response.body()?.success == false) {
                    // Si hay body con success=false, usar ese mensaje
                    response.body()?.message ?: "Error en el registro"
                } else {
                    // Si no hay body, leer errorBody
                    try {
                        val errorBody = response.errorBody()?.string()
                        Log.d("AuthRepository", "Error body: $errorBody")

                        // Intentar parsear el JSON de error
                        if (errorBody?.contains("\"message\"") == true) {
                            val messageRegex = "\"message\"\\s*:\\s*\"([^\"]+)\"".toRegex()
                            messageRegex.find(errorBody)?.groupValues?.get(1) ?: "Error en el registro"
                        } else {
                            "Error en el registro"
                        }
                    } catch (e: Exception) {
                        "Error en el registro"
                    }
                }

                Log.e("AuthRepository", "❌ ERROR: $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "❌ EXCEPCIÓN en registro: ${e.message}", e)
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }

    /**
     * Login usando BACKEND (Spring Boot)
     */
    suspend fun login(username: String, password: String): Result<User> {
        return try {
            Log.d("AuthRepository", "=== INTENTANDO LOGIN ===")
            Log.d("AuthRepository", "Username: $username")
            
            // Crear request
            val request = LoginRequest(
                username = username,
                password = password
            )
            
            Log.d("AuthRepository", "Enviando a backend...")
            
            // Llamar al backend
            val response = apiService.login(request)
            
            Log.d("AuthRepository", "Response code: ${response.code()}")
            
            if (response.isSuccessful && response.body()?.success == true) {
                val loginResponse = response.body()!!

                // Obtener datos desde data o desde campos directos
                val userId = loginResponse.data?.userId ?: loginResponse.userId ?: 0
                val responseUsername = loginResponse.data?.username ?: loginResponse.username ?: username
                val responseEmail = loginResponse.data?.email ?: loginResponse.email ?: ""

                Log.d("AuthRepository", "✅ LOGIN EXITOSO - userId: $userId")

                // Crear user local
                val user = User(
                    id = userId,
                    username = responseUsername,
                    email = responseEmail,
                    password = password
                )
                
                Result.success(user)
            } else {
                // Leer el mensaje de error del backend
                val errorMsg = if (response.body()?.success == false) {
                    // Si hay body con success=false, usar ese mensaje
                    response.body()?.message ?: "Usuario o contraseña incorrectos"
                } else {
                    // Si no hay body, leer errorBody
                    try {
                        val errorBody = response.errorBody()?.string()
                        Log.d("AuthRepository", "Error body: $errorBody")

                        // Intentar parsear el JSON de error
                        if (errorBody?.contains("\"message\"") == true) {
                            val messageRegex = "\"message\"\\s*:\\s*\"([^\"]+)\"".toRegex()
                            messageRegex.find(errorBody)?.groupValues?.get(1) ?: "Usuario o contraseña incorrectos"
                        } else {
                            "Usuario o contraseña incorrectos"
                        }
                    } catch (e: Exception) {
                        "Usuario o contraseña incorrectos"
                    }
                }

                Log.e("AuthRepository", "❌ ERROR: $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "❌ EXCEPCIÓN en login: ${e.message}", e)
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}

