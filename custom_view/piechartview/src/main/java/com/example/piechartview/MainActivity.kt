package com.example.piechartview

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showResult.setOnClickListener(View.OnClickListener {pushDataToCustomView()})

        //Следим за изменениями EditText
        numberArray.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {changeButton()}
        })
    }

    private fun pushDataToCustomView (){
        val text = numberArray.text
        try {
            if (text.isNotEmpty()) {
                val list : ArrayList<Int> = textToList(text.toString()) as ArrayList<Int>
                myCustomView.setList(list)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Не правильный ввод", Toast.LENGTH_SHORT).show()
        }
    }

    private fun changeButton (){
        if (numberArray.text.isNotEmpty()) {
            showResult.isEnabled = true
            showResult.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.activeButton
                )
            )
        } else {
            showResult.isEnabled = false
            showResult.setTextColor(
                ContextCompat.getColor(
                    applicationContext,
                    R.color.nonActiveButton
                )
            )
        }
    }

    //преобразуем текст в List<Int>
    private fun textToList(text: String): List<Int> {
        var newText = text
        if (newText.last() == ',') {
            while (newText.last() == ',') {
                newText = newText.substring(0, newText.length - 1)
            }
        }
        val textArray = newText.split(",")
        return textArray.map { it.toInt() }
    }
}


