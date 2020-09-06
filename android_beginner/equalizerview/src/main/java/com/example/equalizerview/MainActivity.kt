package com.example.equalizerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MyListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        equalizerView.setListener(this)
    }
    override fun onEqualizerDataChanged(value: List<Int>) {
        arrayPercentEqualizerTextView.text = value.toString()
    }
}
