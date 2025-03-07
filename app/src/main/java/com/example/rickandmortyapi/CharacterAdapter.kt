package com.example.rickandmortyapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CharacterAdapter(private val characters: List<Character>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HUMAN -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_human, parent, false)
                HumanViewHolder(view)
            }
            TYPE_ALIEN -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alien, parent, false)
                AlienViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_other, parent, false)
                OtherViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val character = characters[position]
        when (holder) {
            is HumanViewHolder -> holder.bind(character)
            is AlienViewHolder -> holder.bind(character)
            is OtherViewHolder -> holder.bind(character)
        }
    }

    override fun getItemCount(): Int = characters.size

    override fun getItemViewType(position: Int): Int {
        return when (characters[position].species) {
            "Human" -> TYPE_HUMAN
            "Alien" -> TYPE_ALIEN
            else -> TYPE_OTHER
        }
    }

    companion object {
        private const val TYPE_HUMAN = 0
        private const val TYPE_ALIEN = 1
        private const val TYPE_OTHER = 2
    }
}
