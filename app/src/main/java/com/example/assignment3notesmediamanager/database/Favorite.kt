/**
 * Course: MAD204 - Assignment 3
 * Name: Ashish Prajapati
 * Student ID: A00194842
 * Description: Room entity that represents a favorite note entry
 *              pointing to a Note by its id and storing its title.
 */


package com.example.assignment3notesmediamanager.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey(autoGenerate = true) val favId: Int = 0,
    val noteId: Int,
    val title: String
)
