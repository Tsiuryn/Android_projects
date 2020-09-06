package com.example.screenprofile

import android.graphics.*
import com.squareup.picasso.Transformation

class CircularTransformation : Transformation {

    override fun key(): String {
        return "circular"
    }

    override fun transform(source: Bitmap?): Bitmap {
        val size = Math.min(source!!.width, source.height)
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        val bitmap = Bitmap.createBitmap(source, x, y, size, size)
        if (bitmap != source) {
            source.recycle()
        }
        val output = Bitmap.createBitmap(size, size, source.config)
        val canvas = Canvas(output)
        val paint = Paint()
        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
        paint.isAntiAlias = true
        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)
        bitmap.recycle()
        return output
    }
}