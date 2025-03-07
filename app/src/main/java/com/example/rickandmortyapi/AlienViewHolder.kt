package com.example.rickandmortyapi

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AlienViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameTextView: TextView = itemView.findViewById(R.id.name_text)
    private val speciesTextView: TextView = itemView.findViewById(R.id.species_text)
    private val imageView: ImageView = itemView.findViewById(R.id.image_view)

    fun bind(character: Character) {
        nameTextView.text = character.name
        speciesTextView.text = "Species: ${character.species}"
        Glide.with(itemView.context).load(character.image).into(imageView)
    }
}