package com.example.rickandmortyapi.ViewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmortyapi.DataClasses.Character
import com.example.rickandmortyapi.R

class OtherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameTextView: TextView = itemView.findViewById(R.id.name_text)
    private val typeTextView: TextView = itemView.findViewById(R.id.type_text)
    private val imageView: ImageView = itemView.findViewById(R.id.image_view)

    fun bind(character: Character) {
        nameTextView.text = character.name
        typeTextView.text = "Type: ${character.type}"
        Glide.with(itemView.context).load(character.image).into(imageView)
    }
}