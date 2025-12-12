package com.example.assignment3notesmediamanager.database

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

object FileBackupHelper {
    private const val FILE_NAME = "backup.txt"

    fun saveTitles(context: Context, titles: List<String>) {
        val writer = OutputStreamWriter(
            context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
        )
        for (t in titles) {
            writer.write(t)
            writer.write("\n")
        }
        writer.flush()
        writer.close()
    }

    fun loadTitles(context: Context): String {
        return try {
            val input = context.openFileInput(FILE_NAME)
            val reader = BufferedReader(InputStreamReader(input))
            val text = reader.readText()
            reader.close()
            text
        } catch (e: Exception) {
            "No backup file"
        }
    }
}
