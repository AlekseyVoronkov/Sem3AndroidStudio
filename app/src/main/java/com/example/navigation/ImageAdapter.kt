package com.example.navigation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageAdapter(
    private var images: List<GalleryImage>,
    private val viewModel: GalleryViewModel,
    private val onItemClick: (GalleryImage) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = images[position]

        Glide.with(holder.itemView.context)
            .load(image.uri)
            .into(holder.imageView)

        holder.descriptionText.text = image.description.ifEmpty { "No description" }

        // Long click for adding description
        holder.itemView.setOnLongClickListener {
            showDescriptionDialog(holder.itemView.context, position)
            true
        }

        // Click for navigation to detail
        holder.itemView.setOnClickListener {
            onItemClick(image)
        }
    }

    override fun getItemCount() = images.size

    fun updateImages(newImages: List<GalleryImage>) {
        images = newImages
        notifyDataSetChanged()
    }

    private fun showDescriptionDialog(context: Context, position: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_description, null)
        val editText = dialogView.findViewById<EditText>(R.id.descriptionEditText)

        // Установим текущее описание, если оно есть
        images[position].description.let { currentDesc ->
            editText.setText(currentDesc)
            if (currentDesc.isNotEmpty()) {
                editText.setSelection(currentDesc.length)
            }
        }

        AlertDialog.Builder(context)
            .setTitle("Image Description")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val description = editText.text.toString()
                viewModel.updateImageDescription(position, description)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
