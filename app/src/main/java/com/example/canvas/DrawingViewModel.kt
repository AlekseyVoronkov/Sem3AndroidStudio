package com.example.canvas

import android.graphics.Color
import androidx.lifecycle.ViewModel

class DrawingViewModel : ViewModel() {
    var currentColor = Color.BLACK
    var brushSize = 5f
}