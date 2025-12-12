/**
 * Course: MAD204 - Assignment 3
 * Student Name: Ashish Prajapati
 * Student ID: A00194842
 * Date: 2025-12-12
 * Description: RecyclerView adapter that displays all notes with title, content, media thumbnail, and buttons for edit, delete, media pick, and favorite toggle.
 */

package com.example.assignment3notesmediamanager.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment3notesmediamanager.R
import com.example.assignment3notesmediamanager.database.Note

class NoteAdapter(
    private val notes: MutableList<Note>,
    private val onEdit: (Note) -> Unit,
    private val onDelete: (Note) -> Unit,
    private val onPickMedia: (Note) -> Unit,
    private val onFavoriteChanged: (Note, Boolean) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    fun setData(list: List<Note>) {
        notes.clear()
        notes.addAll(list)
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvNoteTitle)
        val tvContent: TextView = view.findViewById(R.id.tvNoteContent)
        val imgMedia: ImageView = view.findViewById(R.id.imgMedia)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
        val btnPickMedia: Button = view.findViewById(R.id.btnPickMedia)
        val cbFavorite: CheckBox = view.findViewById(R.id.cbFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(v)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.tvTitle.text = note.title
        holder.tvContent.text = note.content

        if (note.mediaUri.isNullOrEmpty()) {
            holder.imgMedia.visibility = View.GONE
        } else {
            holder.imgMedia.visibility = View.VISIBLE
            holder.imgMedia.setImageURI(Uri.parse(note.mediaUri))
        }

        holder.btnEdit.setOnClickListener { onEdit(note) }
        holder.btnDelete.setOnClickListener { onDelete(note) }
        holder.btnPickMedia.setOnClickListener { onPickMedia(note) }

        holder.cbFavorite.setOnCheckedChangeListener(null)
        holder.cbFavorite.isChecked = false
        holder.cbFavorite.setOnCheckedChangeListener { _, isChecked ->
            onFavoriteChanged(note, isChecked)
        }
    }
}
