package com.example.notes.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.notes.App.NotesApp
import com.example.notes.Notes.Note
import com.example.notes.R
import com.example.notes.ViewModel.NoteViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteDetailActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var noteViewModel: NoteViewModel

    private lateinit var etTitle: TextInputEditText
    private lateinit var etContent: TextInputEditText

    private var currentNoteId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as NotesApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        // Инициализируем view элементы
        etTitle = findViewById(R.id.et_title)
        etContent = findViewById(R.id.et_content)
        val btnSave = findViewById<android.widget.Button>(R.id.btn_save)

        noteViewModel = ViewModelProvider(this, viewModelFactory)[NoteViewModel::class.java]

        currentNoteId = intent.getIntExtra("note_id", -1).takeIf { it != -1 }

        if (currentNoteId != null) {
            loadNote()
        }

        btnSave.setOnClickListener {
            saveNote()
        }
    }

    private fun loadNote() {
        lifecycleScope.launch {
            currentNoteId?.let { id ->
                noteViewModel.getNoteById(id).collect { note ->
                    note?.let {
                        etTitle.setText(it.title)
                        etContent.setText(it.content)
                    }
                }
            }
        }
    }

    private fun saveNote() {
        val title = etTitle.text.toString()
        val content = etContent.text.toString()

        if (title.isBlank()) {
            etTitle.error = "Title cannot be empty"
            return
        }

        val note = Note(
            id = currentNoteId ?: 0,
            title = title,
            content = content
        )

        if (currentNoteId != null) {
            noteViewModel.update(note)
        } else {
            noteViewModel.insert(note)
        }

        finish()
    }
}