package com.example.watchview

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalTime
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class WatchView(context: Context, attr: AttributeSet) : View(context, attr) {

    companion object{
        const val MARGIN_FOR_LINE = 60
        const val LENGTH_MIN_LINE = 40
        const val TEXT_SIZE = 70F
        const val LENGTH_MIN_SEC_ARROW = 130f
        const val LENGTH_HOUR_ARROW = 200f
    }

    private var secondsAngle = 0f
    private var minutesAngle = 0f
    private var hoursAngle = 0f
    private val path = Path()
    private val paint = Paint()
    private val rect = Rect()
    val mat = Matrix()
    private var minutes: Int = 0
    private var hours = 0
    private var seconds = 0

    private fun changeSecondsAngle(seconds: Int, minutes: Int, hours: Int) {
        this.secondsAngle = seconds * 6f
        this.minutesAngle = minutes * 6f
        this.hoursAngle = angleHours(minutes.toFloat(), hours.toFloat())
        invalidate()
    }

    private fun getCurrentTime (){
        val time = LocalTime.now()
        minutes = time.minute
        seconds = time.second
        hours = if (time.hour > 12) time.hour - 12 else time.hour
    }
    fun startClock (){
        val handler = Handler()
        handler.post(object : Runnable{
            override fun run() {
                getCurrentTime()
                changeSecondsAngle(seconds, minutes, hours)
                handler.postDelayed(this,1000L)
            }
        })
    }

    // метод для расчета поворота часовой стрелки, чтобы она поворачивалась
    //постепенно, а не перепрыгивала на следующий час
    private fun angleHours (minute: Float, hours: Float): Float{
        val percentAngleHours = minute / 60 * 30
        return hours * 30f + percentAngleHours
    }

    override fun onDraw(canvas: Canvas?) {
        val sizeX = canvas!!.width
        val sizeY = canvas.height
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE

        //Радиус окружности
        val radius = if (sizeX > sizeY) sizeY / 2 - 40 else sizeX / 2 - 100

        //Минутные черточки
        val strokeX = sizeX / 2f + radius - MARGIN_FOR_LINE
        path.reset()
        path.moveTo(strokeX - MARGIN_FOR_LINE, sizeY / 2f)
        path.lineTo(strokeX - LENGTH_MIN_LINE, sizeY / 2f)
        paint.color = Color.GREEN
        canvas.drawPath(path, paint)
        for (i in 0 until 59) {
            mat.setRotate(6f, sizeX / 2f, sizeY / 2f)
            path.transform(mat)
            canvas.drawPath(path, paint)
        }
        path.reset()

        //часовые черточки
        path.moveTo(strokeX, sizeY / 2.toFloat())
        path.lineTo(strokeX - MARGIN_FOR_LINE, sizeY / 2.toFloat())
        paint.color = Color.BLACK
        canvas.drawPath(path, paint)
        for (i in 0 until 11) {
            mat.setRotate(30f, sizeX / 2f, sizeY / 2f)
            path.transform(mat)
            canvas.drawPath(path, paint)
        }

        //расставление чисел
        val list = listOf(
            "III", "IV", "V", "VI"
            , "VII", "VIII", "IX", "X", "XI", "XII", "I", "II"
        )
        paint.style = Paint.Style.FILL
        paint.textSize = TEXT_SIZE
        paint.isAntiAlias = true
        var angle = 0
        for (i in list.indices) {
            // Show text
            val myAngleInRad: Float = (angle * PI / 180f).toFloat()
            val x = getXText(radius, sizeX, myAngleInRad)
            val y = getYText(radius, sizeY, myAngleInRad)
            showTime(paint, x, y, list[i], canvas, rect)
            angle += 30
        }
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f

        //часовая стрелка
        hoursArrow(hoursAngle, canvas, paint, path, sizeX.toFloat(), sizeY.toFloat(), radius)

        //минутная стрелка
        minutesArrow(minutesAngle, canvas, paint, path, sizeX.toFloat(), sizeY.toFloat(), radius)

        //секундная стрелка
        secondsArrow(secondsAngle, canvas, paint, path, sizeX.toFloat(), sizeY.toFloat(), radius)

        //круг в центре
        paint.style = Paint.Style.FILL
        paint.color = Color.BLUE
        canvas.drawCircle(sizeX / 2f, sizeY / 2f, 20f, paint)
    }

    private fun secondsArrow(
        angle: Float,
        canvas: Canvas,
        paint: Paint,
        path: Path,
        sizeX: Float,
        sizeY: Float,
        radius: Int
    ) {
        canvas.save()
        paint.color = Color.DKGRAY
        path.reset()
        path.moveTo(sizeX / 2f, sizeY / 2f)
        path.lineTo(sizeX / 2f, sizeY / 2f - radius + LENGTH_MIN_SEC_ARROW)
        canvas.rotate(angle, sizeX / 2f, sizeY / 2f)
        canvas.drawPath(path, paint)
        canvas.restore()
    }

    private fun minutesArrow(
        angle: Float,
        canvas: Canvas,
        paint: Paint,
        path: Path,
        sizeX: Float,
        sizeY: Float,
        radius: Int
    ) {
        canvas.save()
        paint.color = Color.BLUE
        path.reset()
        path.moveTo(sizeX / 2f + 10f, sizeY / 2f)
        path.lineTo(sizeX / 2f, sizeY / 2f - radius + LENGTH_MIN_SEC_ARROW)
        path.lineTo(sizeX / 2f - 10f, sizeY / 2f)
        canvas.rotate(angle, sizeX / 2f, sizeY / 2f)
        canvas.drawPath(path, paint)
        canvas.restore()
    }

    private fun hoursArrow(
        angle: Float,
        canvas: Canvas,
        paint: Paint,
        path: Path,
        sizeX: Float,
        sizeY: Float,
        radius: Int
    ) {
        canvas.save()
        paint.color = Color.RED
        path.reset()
        path.moveTo(sizeX / 2f + 10f, sizeY / 2f)
        path.lineTo(sizeX / 2f, sizeY / 2f - radius + LENGTH_HOUR_ARROW)
        path.lineTo(sizeX / 2f - 10f, sizeY / 2f)
        canvas.rotate(angle, sizeX / 2f, sizeY / 2f)
        canvas.drawPath(path, paint)
        canvas.restore()
    }

    private fun getXText(radius: Int, canvasSizeX: Int, angle: Float): Int {
        val myX = radius * cos(angle.toDouble())
        val x = canvasSizeX / 2 + myX.toInt()
        return x
    }

    private fun getYText(radius: Int, canvasSizeY: Int, angle: Float): Int {
        val myY = radius * sin(angle.toDouble())
        val y = canvasSizeY / 2 + myY.toInt()
        return y
    }

    private fun showTime(
        paint: Paint,
        x: Int,
        y: Int,
        text: String,
        canvas: Canvas,
        rect: Rect
    ) {
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