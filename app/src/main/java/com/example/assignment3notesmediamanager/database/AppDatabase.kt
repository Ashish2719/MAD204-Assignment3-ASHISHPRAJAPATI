/**
 * Course: MAD204 - Assignment 3
 * Student Name: Ashish Prajapati
 * Student ID: A00194842
 * Date: 2025-12-12
 * Description: Room database configuration for the Notes & Media Manager app, including Note and Favorite entities.
 */


package com.example.assignment3notesmediamanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Note::class, Favorite::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "notes_db"
                ).build()
                INSTANCE = inst
                inst
            }
        }
    }
}
