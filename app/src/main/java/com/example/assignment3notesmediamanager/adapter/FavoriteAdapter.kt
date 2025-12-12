/**
 * Course: MAD204 - Assignment 3
 * Student NAme: Ashish Prajapati
 * Student ID: A00194842
 * Date: 2025-12-12
 * Description: RecyclerView adapter for showing the list of favorite notes stored in the Room favorites table.
 */


package com.example.assignment3notesmediamanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment3notesmediamanager.R
import com.example.assignment3notesmediamanager.database.Favorite

class FavoriteAdapter(
    private val favorites: MutableList<Favorite>
) : RecyclerView.Adapter<FavoriteAdapter.FavViewHolder>() {

    fun setData(list: List<Favorite>) {
        favorites.clear()
        favorites.addAll(list)
        notifyDataSetChanged()
    }

    inner class FavViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvFavTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite, parent, false)
        return FavViewHolder(v)
    }

    override fun getItemCount(): Int = favorites.size

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.tvTitle.text = favorites[position].title
    }
}
