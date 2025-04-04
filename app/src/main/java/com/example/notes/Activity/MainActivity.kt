package com.example.notes.Activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes.Notes.NotesAdapter
import com.example.notes.App.NotesApp
import com.example.notes.Notes.Note
import com.example.notes.ViewModel.NoteViewModel
import com.example.notes.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var notesAdapter: NotesAdapter

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as NotesApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация ViewModel
        noteViewModel = ViewModelProvider(this, viewModelFactory)[NoteViewModel::class.java]

        // Инициализация адаптера
        notesAdapter = NotesAdapter(
            onNoteClick = { note ->
                val intent = Intent(this, NoteDetailActivity::class.java).apply {
                    putExtra("note_id", note.id)
                }
                startActivity(intent)
            },
            onNoteLongClick = { note ->
                showDeleteDialog(note)
            }
        )

        setupRecyclerView()
        observeNotes()

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, NoteDetailActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = notesAdapter
        }
    }

    private fun observeNotes() {
        lifecycleScope.launch {
            noteViewModel.allNotes.collectLatest { notes ->
                notesAdapter.submitList(notes)
            }
        }
    }

    private fun showDeleteDialog(note: Note) {
        AlertDialog.Builder(this)
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Delete") { _, _ ->
                noteViewModel.delete(note)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}