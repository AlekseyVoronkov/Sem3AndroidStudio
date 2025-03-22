package com.example.rickandmortyapi.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapi.API.RickAndMortyApiService
import com.example.rickandmortyapi.DataClasses.Character
import kotlinx.coroutines.launch
import com.example.rickandmortyapi.API.RickAndMortyApi

class CharacterViewModel : ViewModel() {

    private val _characters = MutableLiveData<List<Character>>()
    val characters: LiveData<List<Character>> get() = _characters

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // Делаем apiService изменяемым
    var apiService: RickAndMortyApiService = RickAndMortyApi.create()
        private set

    // Метод для установки apiService (используется в тестах)
    fun setApiService(service: RickAndMortyApiService) {
        apiService = service
    }

    fun loadCharacters() {
        viewModelScope.launch {
            try {
                val response = apiService.getCharacters()
                _characters.value = response.results
            } catch (e: Exception) {
                setErrorMessage("Ошибка сети: ${e.message}")
            }
        }
    }

    // Публичный метод для установки ошибки
    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }
}