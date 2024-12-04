package com.example.retrofitforecaster.api

import com.example.retrofitforecaster.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("forecast")
    fun getWeather(@Query("q") city: String,
                   @Query("units") units: String,
                   @Query("appid") apiKey: String): Call<WeatherResponse>
}