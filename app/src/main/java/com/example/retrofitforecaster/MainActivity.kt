package com.example.retrofitforecaster

import WeatherStore
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitforecaster.api.WeatherApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var weatherAdapter: WeatherAdapter
    val weatherStore = WeatherStore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())

        recyclerView = findViewById(R.id.r_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        weatherAdapter = WeatherAdapter()
        recyclerView.adapter = weatherAdapter

        if (weatherStore.weathers == null) {
            fetchWeatherData()
        } else {
            weatherAdapter.submitList(weatherStore.weathers)
            Timber.d("Restored weather data from WeatherStore")
        }
    }

    private fun fetchWeatherData() {
        val units = "metric"
        val api = resources.getString(R.string.api)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherApi = retrofit.create(WeatherApi::class.java)
        val call = weatherApi.getWeather("Шклов", units, api)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        weatherStore.weathers = it.list
                        weatherAdapter.submitList(it.list)
                        Timber.d("Response received: ${it.list}")
                    }
                } else {
                    Timber.e("Error: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Timber.e("Failure: ${t.message}")
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (weatherStore.weathers != null) {
            outState.putSerializable("weatherData", weatherStore.weathers as Serializable)
            Timber.d("Weather data saved in onSaveInstanceState")
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        weatherStore.weathers = savedInstanceState.getSerializable("weatherData") as? List<Weather>
        weatherAdapter.submitList(weatherStore.weathers)
        Timber.d("Weather data restored in onRestoreInstanceState")
    }
}
