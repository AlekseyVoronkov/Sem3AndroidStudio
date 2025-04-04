package com.example.notes.Notes

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDao: NoteDao) {
    fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note) = noteDao.insert(note)

    suspend fun update(note: Note) = noteDao.update(note)

    suspend fun delete(note: Note) = noteDao.delete(note)

    suspend fun getNoteById(id: Int): Note? = noteDao.getNoteById(id)
}