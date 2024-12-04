package com.example.retrofitforecaster

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.bumptech.glide.Glide

class WeatherAdapter : ListAdapter<Weather, RecyclerView.ViewHolder>(WeatherDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        val temperature = getItem(position).main.temp
        return if (temperature > 0) VIEW_TYPE_HOT else VIEW_TYPE_COLD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutId = if (viewType == VIEW_TYPE_HOT) R.layout.weather_item_hot else R.layout.weather_item_cold
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return if (viewType == VIEW_TYPE_HOT) ViewHolderHot(view) else ViewHolderCold(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val weather = getItem(position)
        if (holder is ViewHolderHot) {
            holder.bind(weather)
            holder.itemView.setBackgroundColor(Color.parseColor("#fff6f5"))
        } else if (holder is ViewHolderCold) {
            holder.bind(weather)
        }
    }

    class ViewHolderHot(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.date_text)
        private val tempTextView: TextView = itemView.findViewById(R.id.temp_text)
        private val iconImageView: ImageView = itemView.findViewById(R.id.icon_image)

        fun bind(weather: Weather) {
            dateTextView.text = weather.dt_txt
            tempTextView.text = "${weather.main.temp} °C"
            itemView.setBackgroundColor(Color.parseColor("#fff6f5"))

            Glide.with(itemView.context)
                .load("https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png")
                .into(iconImageView)
        }
    }

    class ViewHolderCold(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.date_text)
        private val tempTextView: TextView = itemView.findViewById(R.id.temp_text)
        private val iconImageView: ImageView = itemView.findViewById(R.id.icon_image)

        fun bind(weather: Weather) {
            dateTextView.text = weather.dt_txt
            tempTextView.text = "${weather.main.temp} °C"
            itemView.setBackgroundColor(Color.parseColor("#a0c4ff"))

            Glide.with(itemView.context)
                .load("https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png")
                .into(iconImageView)
        }
    }

    companion object {
        private const val VIEW_TYPE_HOT = 0
        private const val VIEW_TYPE_COLD = 1
    }
}