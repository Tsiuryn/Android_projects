package com.example.equalizerview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class EqualizerView(context: Context, attr: AttributeSet) : View(context, attr) {

    private val mainTriangle = Paint()
    private val barTriangle = Paint()
    private val fillTriangle = Paint()

    init {
        mainTriangle.style = Paint.Style.STROKE
        mainTriangle.strokeWidth = 10f
        mainTriangle.color = Color.parseColor("#2F80ED")

        barTriangle.style = Paint.Style.STROKE
        barTriangle.color = Color.parseColor("#333333")
        barTriangle.strokeWidth = 10f

        fillTriangle.style = Paint.Style.FILL
        fillTriangle.color = Color.parseColor("#2F80ED")
    }
    private val rect = Rect()
    private var percent = arrayListOf(100, 100, 100, 100, 100)
    private var listener : MyListener? = null
    private var listRect = ArrayList<Rect>()
    private var theFirstListRect = ArrayList<Rect>()

    fun setListener (listener: MyListener){
        this.listener = listener
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        val sizeX = canvas!!.width
        val sizeY = canvas.height

        val indentX: Int
        val indentY: Int
        if (sizeX < sizeY) {
            indentX = 150
            indentY = 150
        } else  {
            indentX = (sizeX -  (sizeY - 200)) / 2
            indentY = 75
        }

        //рисуем прямоугольник
        val rectangleX = sizeX - indentX * 2
        val rectangleY = sizeY - indentY * 2
        val theFirstPointFrame =
            Coordinates(sizeX - rectangleX - indentX, sizeY - rectangleY - indentY)
        val theSecondPointFrame =
            Coordinates(rectangleX + indentX, rectangleY + indentY)
        rect.set(
            theFirstPointFrame.coordX, theFirstPointFrame.coordY
            , theSecondPointFrame.coordX, theSecondPointFrame.coordY
        )
        canvas.drawRect(rect, mainTriangle)

        //рисуем колонку
        val indentationColumnX = rectangleX / 11
        val indentationColumnY = indentationColumnX

        val theFirstPointColumn = Coordinates(
            theFirstPointFrame.coordX + indentationColumnX,
            theFirstPointFrame.coordY + indentationColumnY
        )
        val theSecondPointColumn =
            Coordinates(
                theFirstPointFrame.coordX + indentationColumnX * 2,
                theSecondPointFrame.coordY - indentationColumnY
            )
        rect.set(
            theFirstPointColumn.coordX,
            theFirstPointColumn.coordY,
            theSecondPointColumn.coordX,
            theSecondPointColumn.coordY
        )
        val widthStroke = barTriangle.strokeWidth.toInt()
        canvas.drawRect(rect, barTriangle)
        canvas.save()
        for (i in 0 until 4) {
            canvas.translate(indentationColumnX * 2f, 0f)
            canvas.drawRect(rect, barTriangle)
        }
        canvas.restore()

        //закрашиваем прямоугольник
        var myX = 0
        if (theFirstListRect.isEmpty()) {
            for (i in 0 until 5) {
                val left = theFirstPointColumn.coordX + widthStroke + myX
                val top = theFirstPointColumn.coordY + widthStroke
                val right = theSecondPointColumn.coordX - widthStroke + myX
                val bottom = theSecondPointColumn.coordY - widthStroke

                theFirstListRect.add(Rect(left, top, right, bottom))
                listRect.add(Rect(left, top, right, bottom))
                myX += indentationColumnX * 2
            }
        }
        for (i in 0 until listRect.size) {
            canvas.drawRect(listRect[i], fillTriangle)
        }
    }

    //заполняем прямоугольник
    private fun createRectangleFill(touchX: Int, touchY: Int): Boolean {
        for (i in 0 until listRect.size) {
            if (theFirstListRect[i].contains(touchX, touchY)) {
                percent[i] =
                    calculatePercent(theFirstListRect[i].top, theFirstListRect[i].bottom, touchY)
                Log.d("TAG", "${listRect[i]}")
                Log.d("TAG", "${theFirstListRect[i]}")
                val rect = listRect[i]
                rect.top = touchY
                listRect[i] = rect
                listener?.onEqualizerDataChanged(percent)
                return true
            }
        }

        return false
    }

    private fun calculatePercent (top: Int, bottom: Int, currentY: Int): Int {
        val percent: Double = (bottom - currentY).toDouble() / ( bottom - top ) * 100
        return percent.toInt()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null){
            return false
        }
        val touchX = event.x.toInt()
        val touchY = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                if (createRectangleFill(touchX, touchY)) {
                    invalidate()
                }

            }
            MotionEvent.ACTION_UP -> {
                if (createRectangleFill(touchX, touchY)) {
                    invalidate()
                }
            }
        }
        return true
    }
}

interface MyListener{
    fun onEqualizerDataChanged (value: List<Int>)
}