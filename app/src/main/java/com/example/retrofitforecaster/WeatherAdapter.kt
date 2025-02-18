package com.example.retrofitforecaster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.bumptech.glide.Glide

class WeatherAdapter : ListAdapter<Weather, RecyclerView.ViewHolder>(WeatherDiffCallback()) {

    var isCelsius: Boolean = true
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutId = if (viewType == VIEW_TYPE_HOT) R.layout.weather_item_hot else R.layout.weather_item_cold
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return if (viewType == VIEW_TYPE_HOT) ViewHolderHot(view) else ViewHolderCold(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val weather = getItem(position)
        val temperature = if (isCelsius) weather.main.temp else (weather.main.temp * 9 / 5) + 32
        val unit = if (isCelsius) "°C" else "°F"

        if (holder is ViewHolderHot) {
            holder.bind(weather, temperature, unit)
        } else if (holder is ViewHolderCold) {
            holder.bind(weather, temperature, unit)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val temperature = getItem(position).main.temp
        return if (temperature > 0) VIEW_TYPE_HOT else VIEW_TYPE_COLD
    }

    class ViewHolderHot(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(weather: Weather, temperature: Double, unit: String) {
            itemView.findViewById<TextView>(R.id.date_text).text = weather.dt_txt
            itemView.findViewById<TextView>(R.id.temp_text).text = "$temperature $unit"
            val iconUrl = "https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png"
            Glide.with(itemView.context).load(iconUrl).into(itemView.findViewById(R.id.icon_image))
        }
    }

    class ViewHolderCold(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(weather: Weather, temperature: Double, unit: String) {
            itemView.findViewById<TextView>(R.id.date_text).text = weather.dt_txt
            itemView.findViewById<TextView>(R.id.temp_text).text = "$temperature $unit"
            val iconUrl = "https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png"
            Glide.with(itemView.context).load(iconUrl).into(itemView.findViewById(R.id.icon_image))
        }
    }

    companion object {
        private const val VIEW_TYPE_HOT = 0
        private const val VIEW_TYPE_COLD = 1
    }
}