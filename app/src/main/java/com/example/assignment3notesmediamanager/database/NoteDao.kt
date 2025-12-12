/**
 * Course: MAD204 - Assignment 3
 * Name: Ashish Prajapati
 * Student ID: A00194842
 * Date: 2025-12-12
 * Description: Data Access Object (DAO) interface that defines
 *              CRUD operations for the Note table using Room.
 */

package com.example.assignment3notesmediamanager.database

import androidx.room.*

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes")
    suspend fun getAllNotes(): List<Note>

    @Insert
    suspend fun insertNote(note: Note): Long

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}
