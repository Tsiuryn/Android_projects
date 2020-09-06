package com.example.screenprofile

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import layout.URL_PHOTO



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //подключил стороннюю библиотеку для создания круглого фото с бордером
        val transformation = RoundedTransformationBuilder()
            .borderColor(Color.WHITE)
            .borderWidthDp(2f)
            .cornerRadius(300f)
            .oval(false)
            .build()

        // Изменять фото с помощью picasso.Transformation сложновато (надо разбираться)
        Picasso.get().load(URL_PHOTO).resize(300, 300).transform(transformation).into(photoImageView)
    }
}
