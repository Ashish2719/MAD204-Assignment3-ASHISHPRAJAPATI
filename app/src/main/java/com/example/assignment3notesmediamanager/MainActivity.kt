/**
 * Course: MAD204 - Assignment 3
 * Student Name: Ashish Prajapati
 * Student ID: A00194842
 * Date: 2025-12-12
 * Description: Main screen for the Notes & Media Manager app.Manages user preferences, notes CRUD with Room,
 *              favorites list, media picker, JSON export/import, file backup, and starting the reminder service.
 */


package com.example.assignment3notesmediamanager

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment3notesmediamanager.adapter.FavoriteAdapter
import com.example.assignment3notesmediamanager.adapter.NoteAdapter
import com.example.assignment3notesmediamanager.database.*
import com.example.assignment3notesmediamanager.service.ReminderService
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var swTheme: Switch
    private lateinit var tvUsernameInfo: TextView
    private lateinit var tvThemeInfo: TextView
    private lateinit var tvBackupContent: TextView
    private lateinit var rvNotes: RecyclerView
    private lateinit var rvFavorites: RecyclerView

    private lateinit var db: AppDatabase
    private lateinit var noteDao: NoteDao
    private lateinit var favoriteDao: FavoriteDao

    private lateinit var noteAdapter: NoteAdapter
    private lateinit var favoriteAdapter: FavoriteAdapter

    private val scope = CoroutineScope(Dispatchers.IO)
    private var currentMediaNote: Note? = null

    private val mediaPicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null && currentMediaNote != null) {
                scope.launch {
                    currentMediaNote!!.mediaUri = uri.toString()
                    noteDao.updateNote(currentMediaNote!!)
                    loadData()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etUsername = findViewById(R.id.etUsername)
        swTheme = findViewById(R.id.swTheme)
        tvUsernameInfo = findViewById(R.id.tvUsernameInfo)
        tvThemeInfo = findViewById(R.id.tvThemeInfo)
        tvBackupContent = findViewById(R.id.tvBackupContent)
        rvNotes = findViewById(R.id.rvNotes)
        rvFavorites = findViewById(R.id.rvFavorites)

        val btnSavePrefs: Button = findViewById(R.id.btnSavePrefs)
        val btnAddNote: Button = findViewById(R.id.btnAddNote)
        val btnExportJson: Button = findViewById(R.id.btnExportJson)
        val btnImportJson: Button = findViewById(R.id.btnImportJson)
        val btnBackupFile: Button = findViewById(R.id.btnBackupFile)
        val btnLoadBackup: Button = findViewById(R.id.btnLoadBackup)
        val btnStartReminder: Button = findViewById(R.id.btnStartReminder)

        db = AppDatabase.getInstance(this)
        noteDao = db.noteDao()
        favoriteDao = db.favoriteDao()

        noteAdapter = NoteAdapter(
            mutableListOf(),
            onEdit = { showNoteDialog(it) },
            onDelete = { deleteNote(it) },
            onPickMedia = { pickMedia(it) },
            onFavoriteChanged = { note, fav -> handleFavorite(note, fav) }
        )
        favoriteAdapter = FavoriteAdapter(mutableListOf())

        rvNotes.layoutManager = LinearLayoutManager(this)
        rvNotes.adapter = noteAdapter
        rvFavorites.layoutManager = LinearLayoutManager(this)
        rvFavorites.adapter = favoriteAdapter

        btnSavePrefs.setOnClickListener { savePrefs() }
        btnAddNote.setOnClickListener { showNoteDialog(null) }
        btnExportJson.setOnClickListener { exportJson() }
        btnImportJson.setOnClickListener { importJson() }
        btnBackupFile.setOnClickListener { backupToFile() }
        btnLoadBackup.setOnClickListener { loadBackupFromFile() }
        btnStartReminder.setOnClickListener {
            startService(android.content.Intent(this, ReminderService::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadPrefs()
        loadData()
    }

    private fun savePrefs() {
        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putString("username", etUsername.text.toString())
            .putBoolean("dark_theme", swTheme.isChecked)
            .apply()
        loadPrefs()
    }

    private fun loadPrefs() {
        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val name = prefs.getString("username", "")
        val dark = prefs.getBoolean("dark_theme", false)

        etUsername.setText(name)
        swTheme.isChecked = dark
        tvUsernameInfo.text = "Username: $name"
        tvThemeInfo.text = if (dark) "Theme: Dark" else "Theme: Light"

        if (dark) {
            // Dark background, light text
            window.decorView.setBackgroundColor(
                resources.getColor(android.R.color.black, theme)
            )
            val light = resources.getColor(android.R.color.white, theme)
            etUsername.setTextColor(light)
            tvUsernameInfo.setTextColor(light)
            tvThemeInfo.setTextColor(light)
        } else {
            // Light background, dark text
            window.decorView.setBackgroundColor(
                resources.getColor(android.R.color.white, theme)
            )
            val darkColor = resources.getColor(android.R.color.black, theme)
            etUsername.setTextColor(darkColor)
            tvUsernameInfo.setTextColor(darkColor)
            tvThemeInfo.setTextColor(darkColor)
        }
    }


    private fun showNoteDialog(note: Note?) {
        val view = layoutInflater.inflate(R.layout.dialog_add_note, null)
        val etTitle: EditText = view.findViewById(R.id.etDialogTitle)
        val etContent: EditText = view.findViewById(R.id.etDialogContent)

        if (note != null) {
            etTitle.setText(note.title)
            etContent.setText(note.content)
        }

        AlertDialog.Builder(this)
            .setTitle(if (note == null) "New note" else "Edit note")
            .setView(view)
            .setPositiveButton("Save") { _, _ ->
                val t = etTitle.text.toString()
                val c = etContent.text.toString()
                scope.launch {
                    if (note == null) {
                        noteDao.insertNote(Note(title = t, content = c))
                    } else {
                        note.title = t
                        note.content = c
                        noteDao.updateNote(note)
                    }
                    loadData()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteNote(note: Note) {
        scope.launch {
            noteDao.deleteNote(note)
            val fav = favoriteDao.getFavoriteForNote(note.id)
            if (fav != null) favoriteDao.deleteFavorite(fav)
            loadData()
        }
    }

    private fun pickMedia(note: Note) {
        currentMediaNote = note
        mediaPicker.launch("image/*")
    }

    private fun handleFavorite(note: Note, fav: Boolean) {
        scope.launch {
            val existing = favoriteDao.getFavoriteForNote(note.id)
            if (fav && existing == null) {
                favoriteDao.insertFavorite(Favorite(noteId = note.id, title = note.title))
            } else if (!fav && existing != null) {
                favoriteDao.deleteFavorite(existing)
            }
            loadData()
        }
    }

    private fun loadData() {
        scope.launch {
            val notes = noteDao.getAllNotes()
            val favs = favoriteDao.getAllFavorites()
            withContext(Dispatchers.Main) {
                noteAdapter.setData(notes)
                favoriteAdapter.setData(favs)
            }
        }
    }

    private fun exportJson() {
        scope.launch {
            val notes = noteDao.getAllNotes()
            val json = JsonHelper.notesToJson(notes)
            android.util.Log.d("JSON_EXPORT", json)
        }
    }

    private fun importJson() {
        Toast.makeText(this, "JSON import placeholder", Toast.LENGTH_SHORT).show()
    }

    private fun backupToFile() {
        scope.launch {
            val notes = noteDao.getAllNotes()
            val titles = notes.map { it.title }
            FileBackupHelper.saveTitles(this@MainActivity, titles)
        }
    }

    private fun loadBackupFromFile() {
        tvBackupContent.text = FileBackupHelper.loadTitles(this)
    }
}
