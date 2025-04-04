package com.example.notes.App

import android.app.Application
import com.example.notes.Activity.NoteDetailActivity
import com.example.notes.Activity.MainActivity
import com.example.notes.Notes.NoteDao
import com.example.notes.ViewModel.NoteViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton @Component (modules = [AppModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: NoteDetailActivity)
    fun inject(viewModel: NoteViewModel)

    fun noteDao(): NoteDao
    fun application(): Application
}