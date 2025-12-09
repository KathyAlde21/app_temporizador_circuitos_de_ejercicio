package com.example.circuitosejercicio.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.circuitosejercicio.data.local.dao.TimerDao
import com.example.circuitosejercicio.data.local.entity.TimerEntity

@Database(
    entities = [TimerEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun timerDao(): TimerDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "circuitos_db"
                )
                    // Para este ejercicio, si cambias el esquema,
                    // se borra y recrea la BD (más simple que manejar migraciones).
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
