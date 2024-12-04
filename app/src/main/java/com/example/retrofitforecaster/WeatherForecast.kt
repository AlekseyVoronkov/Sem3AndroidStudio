package com.example.retrofitforecaster

data class WeatherResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<Weather>
)

data class Weather(
    val dt: Long,
    val main: Main,
    val weather: List<WeatherDetail>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    val rain: Rain?,
    val sys: Sys,
    val dt_txt: String
)