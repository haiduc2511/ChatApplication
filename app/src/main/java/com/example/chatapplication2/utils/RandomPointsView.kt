package com.example.chatapplication2.utils
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.example.chatapplication2.model.Group

class RandomPointsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val points = mutableListOf<ColoredPoint>()
    private val pointRadius = 30f
    var onPointClickListener: ((x: Float, y: Float) -> Unit)? = null

    // Method to add a point with a specific color
    fun addPoint(x: Float, y: Float, color: Int) {
        points.add(ColoredPoint(x, y, color))
        Log.d("check toạ độ point", "${points.toString()}")
        invalidate() // Redraw the view
    }
    fun clearPoints() {
        points.clear() // Xóa tất cả các điểm trong danh sách
        invalidate()   // Làm mới giao diện
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        points.forEach { point ->
            paint.color = point.color
            val screenX = width * point.x / 100
            val screenY = height * point.y / 100
            Log.d("check toạ độ point", "$width + $height + ${point.toString()}")
            canvas.drawCircle(screenX, screenY, pointRadius, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val touchX = event.x
            val touchY = event.y

            // Check if the touch is near any point
            points.forEach { point ->
                if (distance(point.x, point.y, touchX, touchY) <= pointRadius) {
                    onPointClickListener?.invoke(point.x, point.y)
//                    Toast.makeText(context, "Clicked: (${point.x}, ${point.y})", Toast.LENGTH_SHORT).show()
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    // Helper function to calculate distance between two points
    private fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val screenX = width * x1 / 100
        val screenY = height * y1 / 100
        return Math.hypot((x2 - screenX).toDouble(), (y2 - screenY).toDouble()).toFloat()
    }

    // Data class to represent a colored point
    private data class ColoredPoint(val x: Float, val y: Float, val color: Int)
}