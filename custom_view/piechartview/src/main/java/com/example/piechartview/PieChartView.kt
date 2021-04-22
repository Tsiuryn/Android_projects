package com.example.piechartview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.PI

class PieChartView(context: Context, attr: AttributeSet) : View(context, attr) {
    private var list = ArrayList<Int>()
    private val circlePaint = Paint()
    private val textPaint = Paint()

    fun setList(list: ArrayList<Int>) {
        this.list = list
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        // находим размеры холста
        val sizeX = canvas!!.width
        val sizeY = canvas.height
        //Если массив числе пустой, тогда выводим текст "No data"
        if (list.isEmpty()) {
            textPaint.textSize = 60f
            textPaint.color = Color.BLACK
            textPaint.textAlign = Paint.Align.CENTER
            canvas.drawText("No data", (sizeX / 2).toFloat(), (sizeY / 2).toFloat(), textPaint)
            return
        }
        //Находим радиус круга, отступая от границ холста на 40
        val radius = if (sizeX > sizeY) sizeY / 2 - 40 else sizeX / 2 - 40
        //создаем прямоугольник, в который будет вписан круг
        val rectF = RectF()
        //создаем прямоугольник, в котором будет текст - проценты
        val rect = Rect()
        //задаем границы прямоугольника (для круга)
        val left: Float = (sizeX / 2 - radius).toFloat()
        val right: Float = (sizeX / 2 + radius).toFloat()
        val top: Float = (sizeY / 2 - radius).toFloat()
        val bottom: Float = (sizeY / 2 + radius).toFloat()
        rectF.set(left, top, right, bottom)
        val random = Random()
        var angle = 0f
        if (list.isNotEmpty()) {
            val sum = list.sum()
            for (i in 0 until list.size) {
                val percent: Float = list[i] * 100f / (sum * 100)
                val currentAngle = 360f * percent
                circlePaint.setARGB(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
                canvas.drawArc(rectF, angle, currentAngle, true, circlePaint)
                // Show text
                val myPercent = String.format("%.0f", percent * 100)
                val myAngleInRad: Float = ((angle + currentAngle / 2) * PI / 180f).toFloat()
                val x = getXText(radius, sizeX, myAngleInRad)
                val y = getYText(radius, sizeY, myAngleInRad)
                showPercentOfThePie(textPaint, x, y, "$myPercent%", canvas, rect)
                angle += currentAngle
            }
        }
    }

    private fun getXText(radius: Int, canvasSizeX: Int, angle: Float): Int {
        val myRadius = radius / 2
        val myX = myRadius * Math.cos(angle.toDouble())
        val x = canvasSizeX / 2 + myX.toInt()
        return x
    }

    private fun getYText(radius: Int, canvasSizeY: Int, angle: Float): Int {
        val myRadius = radius / 2
        val myY = myRadius * Math.sin(angle.toDouble())
        val y = canvasSizeY / 2 + myY.toInt()
        return y
    }

    private fun showPercentOfThePie(
        paint: Paint,
        x: Int,
        y: Int,
        text: String,
        canvas: Canvas,
        rect: Rect
    ) {
        paint.textSize = 50f
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint.getTextBounds(text, 0, text.length, rect)
        val textWidth = paint.measureText(text)
        val textHeight = rect.height()
        canvas.drawText(
            text,
            x.toFloat() - (textWidth / 2f),
            y.toFloat() + (textHeight / 2f),
            paint
        )
    }

}
