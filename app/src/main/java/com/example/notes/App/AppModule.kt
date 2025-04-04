package com.example.notes.App

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notes.Notes.NoteDatabase
import com.example.notes.Notes.NoteDao
import com.example.notes.Notes.NoteRepository
import com.example.notes.ViewModel.NoteViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {
    @Provides @Singleton
    fun provideApplication(): Application = application

    @Provides @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase =
        NoteDatabase.getDatabase(app)

    @Provides @Singleton
    fun provideNoteDao(database: NoteDatabase): NoteDao = database.noteDao()

    @Provides @Singleton
    fun provideViewModelFactory(
        noteDao: NoteDao
    ): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
                    return NoteViewModel(NoteRepository(noteDao)) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}