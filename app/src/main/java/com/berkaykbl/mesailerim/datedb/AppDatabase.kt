package com.berkaykbl.mesailerim.datedb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.berkaykbl.mesailerim.datedb.DateDao
import com.berkaykbl.mesailerim.datedb.DateEntity

@Database(entities = [DateEntity::class, SettingsEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dateDao(): DateDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
