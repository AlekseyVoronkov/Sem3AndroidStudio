package com.example.notes.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.Notes.Note
import com.example.notes.Notes.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteViewModel @Inject constructor(private val repository: NoteRepository) : ViewModel() {
    val allNotes: Flow<List<Note>> = repository.getAllNotes()

    fun insert(note: Note) = viewModelScope.launch {
        repository.insert(note)
    }

    fun update(note: Note) = viewModelScope.launch {
        repository.update(note)
    }

    fun delete(note: Note) = viewModelScope.launch {
        repository.delete(note)
    }

    fun getNoteById(id: Int): Flow<Note?> = flow {
        emit(repository.getNoteById(id))
    }.flowOn(Dispatchers.IO)
}