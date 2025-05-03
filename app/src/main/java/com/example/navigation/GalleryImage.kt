package com.example.gallery.model

import android.net.Uri

/**
 * Data class representing an image in the gallery
 * @param uri
 * @param name Name of the image file
 * @param description
 */
data class GalleryImage(
    val uri: Uri,
    val name: String,
    val description: String = ""
) {
    /**
     * Returns a copy of the image with updated description
     */
    fun updateDescription(newDescription: String): GalleryImage {
        return this.copy(description = newDescription)
    }
}