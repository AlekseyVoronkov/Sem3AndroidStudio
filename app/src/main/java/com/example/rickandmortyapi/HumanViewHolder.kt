package com.example.rickandmortyapi

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class HumanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameTextView: TextView = itemView.findViewById(R.id.name_text)
    private val statusTextView: TextView = itemView.findViewById(R.id.status_text)
    private val imageView: ImageView = itemView.findViewById(R.id.image_view)

    fun bind(character: Character) {
        nameTextView.text = character.name
        statusTextView.text = "Status: ${character.status}"
        Glide.with(itemView.context).load(character.image).into(imageView)
    }
}