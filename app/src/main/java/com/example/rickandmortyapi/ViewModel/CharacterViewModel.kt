package com.example.rickandmortyapi.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rickandmortyapi.DataClasses.Character
import com.example.rickandmortyapi.API.CharacterResponse
import com.example.rickandmortyapi.API.RickAndMortyApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CharacterViewModel : ViewModel() {

    private val _characters = MutableLiveData<List<Character>>()
    val characters: LiveData<List<Character>> get() = _characters

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://rickandmortyapi.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(RickAndMortyApi::class.java)

    fun loadCharacters() {
        val call = api.getCharacters()
        call.enqueue(object : Callback<CharacterResponse> {
            override fun onResponse(call: Call<CharacterResponse>, response: Response<CharacterResponse>) {
                if (response.isSuccessful) {
                    _characters.value = response.body()?.results
                } else {
                    _errorMessage.value = "Ошибка: ${response.errorBody()?.string()}"
                }
            }

            override fun onFailure(call: Call<CharacterResponse>, t: Throwable) {
                _errorMessage.value = "Ошибка сети: ${t.message}"
            }
        })
    }
}