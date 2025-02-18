package com.example.retrofitforecaster

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var weatherAdapter: WeatherAdapter
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.r_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        weatherAdapter = WeatherAdapter()
        recyclerView.adapter = weatherAdapter

        viewModel.weatherData.observe(this) { weatherList ->
            weatherList?.let {
                weatherAdapter.submitList(it)
                Timber.d("Weather data updated: ${it.size} items")
            }
        }

        viewModel.errorMessage.observe(this) { error ->
            error?.let {
                Timber.e("Error: $it")
                Toast.makeText(this, "Error: $it", Toast.LENGTH_SHORT).show()
            }
        }

        val fetchButton = findViewById<Button>(R.id.fetch_button)
        fetchButton.setOnClickListener {
            val city = findViewById<EditText>(R.id.city_input).text.toString()
            if (city.isNotEmpty()) {
                val units = "metric"
                val apiKey = resources.getString(R.string.api)
                viewModel.fetchWeather(city, units, apiKey)
            } else {
                Timber.e("Поле города пустое")
            }
        }

        val temperatureSwitch = findViewById<Switch>(R.id.temperature_switch)
        temperatureSwitch.setOnCheckedChangeListener { _, isChecked ->
            weatherAdapter.isCelsius = !isChecked
        }
    }
}