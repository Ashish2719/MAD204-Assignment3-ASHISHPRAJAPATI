/**
 * Course: MAD204 - Assignment 3
 * Student Name: Ashish Prajapati
 * Student ID: A00194842
 * Date: 2025-12-12
 * Description: Room entity that represents a note with id, title,
 *              content text, and optional media URI string.
 */

package com.example.assignment3notesmediamanager.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var title: String,
    var content: String,
    var mediaUri: String? = null
)
