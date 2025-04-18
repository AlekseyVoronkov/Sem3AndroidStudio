package com.example.canvas

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var drawingView: DrawingView
    private lateinit var viewModel: DrawingViewModel

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    drawingView.setBackgroundBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(DrawingViewModel::class.java)
        drawingView = findViewById(R.id.drawingView)

        setupColorButtons()
        setupBrushSizeSeekBar()
        setupActionButtons()
    }

    private fun setupColorButtons() {
        val colorButtons = listOf(
            findViewById<ImageView>(R.id.blackColor),
            findViewById<ImageView>(R.id.redColor),
            findViewById<ImageView>(R.id.blueColor),
            findViewById<ImageView>(R.id.greenColor)
        )

        colorButtons.forEach { button ->
            button.setOnClickListener {
                when (button.id) {
                    R.id.blackColor -> viewModel.currentColor = Color.BLACK
                    R.id.redColor -> viewModel.currentColor = Color.RED
                    R.id.blueColor -> viewModel.currentColor = Color.BLUE
                    R.id.greenColor -> viewModel.currentColor = Color.GREEN
                }
                drawingView.setColor(viewModel.currentColor)
            }
        }
    }

    private fun setupBrushSizeSeekBar() {
        val seekBar = findViewById<SeekBar>(R.id.brushSizeSeekBar)
        seekBar.progress = viewModel.brushSize.toInt()
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.brushSize = progress.toFloat()
                drawingView.setBrushSize(viewModel.brushSize)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setupActionButtons() {
        findViewById<Button>(R.id.clearButton).setOnClickListener {
            drawingView.clear()
        }

        findViewById<Button>(R.id.saveButton).setOnClickListener {
            saveDrawing()
        }

        findViewById<Button>(R.id.loadButton).setOnClickListener {
            loadImageFromGallery()
        }
    }

    private fun saveDrawing() {
        val bitmap = drawingView.getBitmap()
        val saved = MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            "Drawing_${System.currentTimeMillis()}.jpg",
            "Drawing from Drawing App"
        )

        if (saved != null) {
            Toast.makeText(this, "Рисунок сохранён в галерею", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }
}