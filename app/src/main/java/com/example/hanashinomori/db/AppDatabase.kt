package com.example.hanashinomori.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.hanashinomori.dao.MediaItemDao
import com.example.hanashinomori.dao.UserDao
import com.example.hanashinomori.entity.MediaItem
import com.example.hanashinomori.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [User::class, MediaItem::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun mediaItemDao(): MediaItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hanashinomori_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database.mediaItemDao())
                }
            }
        }

        suspend fun populateDatabase(mediaItemDao: MediaItemDao) {
            // Libros
            mediaItemDao.insert(
                MediaItem(title = "1984", author = "George Orwell", category = "Libro")
            )
            mediaItemDao.insert(
                MediaItem(title = "Cien Años de Soledad", author = "Gabriel García Márquez", category = "Libro")
            )
            mediaItemDao.insert(
                MediaItem(title = "El Principito", author = "Antoine de Saint-Exupéry", category = "Libro")
            )

            // Mangas
            mediaItemDao.insert(
                MediaItem(title = "Naruto", author = "Masashi Kishimoto", category = "Manga")
            )
            mediaItemDao.insert(
                MediaItem(title = "One Piece", author = "Eiichiro Oda", category = "Manga")
            )
            mediaItemDao.insert(
                MediaItem(title = "Death Note", author = "Tsugumi Ohba", category = "Manga")
            )

            // Manhwas
            mediaItemDao.insert(
                MediaItem(title = "Solo Leveling", author = "Chugong", category = "Manhwa")
            )
            mediaItemDao.insert(
                MediaItem(title = "Tower of God", author = "SIU", category = "Manhwa")
            )
            mediaItemDao.insert(
                MediaItem(title = "The Beginning After The End", author = "TurtleMe", category = "Manhwa")
            )

            // Donghuas
            mediaItemDao.insert(
                MediaItem(title = "Battle Through The Heavens", author = "Tiancan Tudou", category = "Donghua")
            )
            mediaItemDao.insert(
                MediaItem(title = "Tales of Demons and Gods", author = "Mad Snail", category = "Donghua")
            )
            mediaItemDao.insert(
                MediaItem(title = "Soul Land", author = "Tang Jia San Shao", category = "Donghua")
            )
        }
    }
}

