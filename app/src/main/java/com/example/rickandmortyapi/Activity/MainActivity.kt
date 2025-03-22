package com.example.rickandmortyapi.Activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortyapi.Adapter.CharacterAdapter
import com.example.rickandmortyapi.R
import com.example.rickandmortyapi.ViewModel.CharacterViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CharacterAdapter
    val viewModel: CharacterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CharacterAdapter(emptyList())
        recyclerView.adapter = adapter

        viewModel.characters.observe(this, Observer { characters ->
            characters?.let {
                adapter = CharacterAdapter(characters)
                recyclerView.adapter = adapter
            }
        })

        viewModel.errorMessage.observe(this, Observer { error ->
            error?.let {
                Snackbar.make(
                    findViewById(R.id.main),
                    error,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })

        viewModel.loadCharacters()
    }
}