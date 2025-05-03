package com.example.navigation

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GalleryViewModel : ViewModel() {
    private val _images = MutableLiveData<List<GalleryImage>>()
    val images: LiveData<List<GalleryImage>> = _images

    fun loadImages(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val imageList = mutableListOf<GalleryImage>()

            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED
            )

            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

            context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    imageList.add(GalleryImage(contentUri, name, ""))
                }
            }

            _images.postValue(imageList)
        }
    }
    private val _selectedImage = MutableLiveData<GalleryImage?>()
    val selectedImage: LiveData<GalleryImage?> = _selectedImage

    fun selectImage(image: GalleryImage) {
        _selectedImage.value = image
    }

    fun updateImageDescription(position: Int, description: String) {
        val currentList = _images.value?.toMutableList() ?: return
        if (position in currentList.indices) {
            currentList[position] = currentList[position].copy(description = description)
            _images.value = currentList
        }
    }
}


data class GalleryImage(
    val uri: Uri,
    val name: String,
    val description: String
)