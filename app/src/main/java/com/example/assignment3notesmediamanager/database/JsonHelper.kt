
/**
 * Course: MAD204 - Assignment 3
 * Name: Ashish Prajapati
 * Student ID: A00194842
 * Description: Helper singleton that uses GSON to convert a list
 *              of Note objects to JSON and back for backup/restore.
 */


package com.example.assignment3notesmediamanager.database

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object JsonHelper {
    private val gson = Gson()

    fun notesToJson(notes: List<Note>): String = gson.toJson(notes)

    fun jsonToNotes(json: String): List<Note> {
        val type = object : TypeToken<List<Note>>() {}.type
        return gson.fromJson(json, type)
    }
}
