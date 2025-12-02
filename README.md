# HanashiNoMori - Android App

Aplicación móvil para gestión de biblioteca digital de manga, manhwa y donghua.

## 📱 Características

- **Autenticación**: Login y registro de usuarios con encriptación BCrypt
- **Biblioteca Digital**: Catálogo de 30+ títulos organizados por categorías
- **Favoritos**: Sistema de favoritos personal por usuario
- **Escáner QR**: Agregar libros escaneando códigos QR
- **Panel Admin**: Gestión completa de usuarios y libros (solo administradores)

## 🛠️ Tecnologías

- **Kotlin** + Jetpack Compose
- **MVVM Architecture**
- **Retrofit** para consumo de API REST
- **CameraX** + ML Kit para escaneo de QR
- **Material Design 3**

## 📦 Requisitos

- Android Studio Hedgehog o superior
- JDK 17
- Android SDK 34
- Emulador o dispositivo con Android 8.0+ (API 26)

## 🚀 Instalación

1. Clonar el repositorio
2. Abrir en Android Studio
3. Configurar el backend (ver sección Backend)
4. Ejecutar: `./gradlew installDebug`

## 🔧 Configuración

### Backend API

La app se conecta a un backend Spring Boot en:
```
http://10.0.2.2:8080 (emulador)
http://localhost:8080 (dispositivo físico en red local)
```

Configurar en: `app/src/main/java/com/example/hanashinomori/network/RetrofitClient.kt`

### Permisos

- `INTERNET`: Comunicación con API
- `CAMERA`: Escaneo de códigos QR

## 📊 Estructura del Proyecto

```
app/src/main/java/com/example/hanashinomori/
├── model/          # Modelos de datos
├── network/        # Configuración Retrofit y API
├── repository/     # Repositorios (AuthRepository, BookRepository, AdminRepository)
├── view/           # Pantallas Compose UI
├── viewmodel/      # ViewModels (MVVM)
└── MainActivity.kt # Activity principal
```

## 🎯 Funcionalidades Principales

### Usuario Regular
- Login/Registro
- Explorar catálogo por categorías (Manga, Manhwa, Donghua)
- Ver detalles de libros
- Agregar/eliminar favoritos
- Escanear QR para agregar libros
- Búsqueda por título

### Administrador
- Todas las funcionalidades de usuario regular
- Crear/editar/eliminar libros
- Crear/editar/eliminar usuarios
- Asignar roles de administrador

## 🔐 Credenciales de Prueba

**Usuario Regular:**
- Email: `user@test.com`
- Password: `123456`

**Administrador:**
- Email: `admin@hanashinomori.com`
- Password: `admin123`

## 📱 Formato QR

Para agregar libros mediante QR, el código debe contener:
```json
{"bookId": 1}
```

Donde `bookId` es el ID del libro en la base de datos.

## 🧪 Testing

```bash
# Ejecutar tests unitarios
./gradlew test

# Ejecutar tests instrumentados
./gradlew connectedAndroidTest
```

## 📝 Notas de Desarrollo

- La app requiere conexión a internet para funcionar
- El backend debe estar corriendo antes de usar la app
- Para usar el escáner QR en emulador, configurar cámara como VirtualScene
- Los logs de debug están disponibles con el tag: `HanashiNoMori`

## 🔄 Versiones

**Versión Actual:** 1.0.0
- Sistema de autenticación completo
- CRUD de favoritos
- Escáner QR funcional
- Panel de administración

## 👥 Autores

Desarrollado como proyecto de Aplicaciones Móviles - DuocUC

---

**Fecha de actualización:** Diciembre 2024

