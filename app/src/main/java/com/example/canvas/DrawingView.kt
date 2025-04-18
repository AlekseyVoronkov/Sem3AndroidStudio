package com.example.canvas

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var currentPath: Path = Path()
    private val drawingPaths = mutableListOf<DrawingPath>()
    private var currentColor = Color.BLACK
    private var currentBrushSize = 5f
    private var canvasBitmap: Bitmap? = null
    private var drawCanvas: Canvas? = null
    private val paint = Paint().apply {
        color = currentColor
        strokeWidth = currentBrushSize
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvasBitmap?.let { canvas.drawBitmap(it, 0f, 0f, paint) }

        for (path in drawingPaths) {
            paint.color = path.color
            paint.strokeWidth = path.brushSize
            canvas.drawPath(path.path, paint)
        }

        paint.color = currentColor
        paint.strokeWidth = currentBrushSize
        canvas.drawPath(currentPath, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentPath.moveTo(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                currentPath.lineTo(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                drawingPaths.add(DrawingPath(currentPath, currentColor, currentBrushSize))
                currentPath = Path()
                invalidate()
            }
        }
        return true
    }

    fun setColor(newColor: Int) {
        currentColor = newColor
    }

    fun setBrushSize(newSize: Float) {
        currentBrushSize = newSize
    }

    fun setBackgroundBitmap(bitmap: Bitmap) {
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
        canvasBitmap = resizedBitmap.copy(Bitmap.Config.ARGB_8888, true)
        drawCanvas = Canvas(canvasBitmap!!)
        invalidate()
    }

    fun getBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return bitmap
    }

    fun clear() {
        drawingPaths.clear()
        canvasBitmap?.eraseColor(Color.TRANSPARENT)
        invalidate()
    }

    private data class DrawingPath(val path: Path, val color: Int, val brushSize: Float)
}