/**
 * Course: MAD204 - Assignment 3
 * Name: Ashish Prajapati
 * Student ID: A00194842
 * Description: DAO interface that manages insert, delete, and
 *              query operations for favorite note records.
 */

package com.example.assignment3notesmediamanager.database

import androidx.room.*

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites")
    suspend fun getAllFavorites(): List<Favorite>

    @Insert
    suspend fun insertFavorite(favorite: Favorite): Long

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

    @Query("SELECT * FROM favorites WHERE noteId = :noteId LIMIT 1")
    suspend fun getFavoriteForNote(noteId: Int): Favorite?
}
