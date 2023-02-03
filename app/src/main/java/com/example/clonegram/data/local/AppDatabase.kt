package com.example.clonegram.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.clonegram.data.local.models.ContactDbModel
import com.example.clonegram.data.local.models.UserDbModel

@Database(entities = [ContactDbModel::class, UserDbModel::class], version = 13, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private const val DB_NAME = "AppDatabase"
        private val lock = Any()
        private var db: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            kotlin.synchronized(lock) {
                db?.let { return it }
                val instance = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
                db = instance
                return instance
            }
        }
    }

    abstract fun contactDao(): ContactDao
    abstract fun userDao(): UserDao
}