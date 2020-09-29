package com.example.helper.ui.normalization

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.helper.R
import kotlinx.android.synthetic.main.fragment_normalization.*

class NormalizationFragment : Fragment(), View.OnClickListener {
    private var rbArray = ArrayList<RadioButton>()
    private var edtArray = ArrayList<EditText>()
    private var tvArray = ArrayList<TextView>()
    private var nameOfFields = ArrayList<String>()
    private val calculate = CalculateNormalization()
    private lateinit var checkedButton: RadioButton
    private var canCalc = false

    private var qCream = 0.0
    private var qMilk = 0.0
    private var qSkim = 0.0
    private var fCream = 0.0
    private var fMilk = 0.0
    private var fSkim = 0.0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_normalization, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillArrayList()
        createNameOfFields()
        radioIsChecked(radio_quant_cream)
        radio_quant_cream.setOnClickListener(this)
        radio_quant_milk.setOnClickListener(this)
        radio_quant_skim.setOnClickListener(this)
        radio_fat_cream.setOnClickListener(this)
        radio_fat_milk.setOnClickListener(this)
        radio_fat_skim.setOnClickListener(this)
        quant_cream.setOnClickListener(this)
        quant_milk.setOnClickListener(this)
        quant_skim.setOnClickListener(this)
        fat_cream.setOnClickListener(this)
        fat_milk.setOnClickListener(this)
        fat_skim.setOnClickListener(this)
        info.setOnClickListener(this)
        quant_cream.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                fillTriangle(quant_cream)
                if (checkedButton !== radio_quant_cream && canCalc) {
                    changeFieldToZero(quant_cream)
                    calculateFields(quant_cream)
                }

            }
        })
        quant_milk.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                fillTriangle(quant_milk)
                if (checkedButton !== radio_quant_milk && canCalc) {
                    changeFieldToZero(quant_milk)
                    calculateFields(quant_milk)
                }
            }
        })
        quant_skim.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                fillTriangle(quant_skim)
                if (checkedButton !== radio_quant_skim && canCalc) {
                    changeFieldToZero(quant_skim)
                    calculateFields(quant_skim)
                }
            }
        })
        fat_cream.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                fillTriangle(fat_cream)
                if (checkedButton !== radio_fat_cream && canCalc) {
                    calculateFields(fat_cream)
                }
            }
        })
        fat_milk.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                fillTriangle(fat_milk)
                if (checkedButton !== radio_fat_milk && canCalc) {
                    calculateFields(fat_milk)
                }
            }
        })
        fat_skim.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                fillTriangle(fat_skim)
                if (checkedButton !== radio_fat_skim && canCalc) {
                    calculateFields(fat_skim)
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v) {
            radio_quant_cream -> {
                radioIsChecked(radio_quant_cream)
            }
            radio_quant_milk -> {
                radioIsChecked(radio_quant_milk)
            }
            radio_quant_skim -> {
                radioIsChecked(radio_quant_skim)
            }
            radio_fat_cream -> {
                radioIsChecked(radio_fat_cream)
            }
            radio_fat_milk -> {
                radioIsChecked(radio_fat_milk)
            }
            radio_fat_skim -> {
                radioIsChecked(radio_fat_skim)
            }
            quant_cream -> quant_cream.setText("")
            quant_milk -> quant_milk.setText("")
            quant_skim -> quant_skim.setText("")
            fat_cream -> fat_cream.setText("")
            fat_milk -> fat_milk.setText("")
            fat_skim -> fat_skim.setText("")
            info -> fragmentManager!!.beginTransaction()
                    .add(R.id.nav_host_fragment, NormalizationInfoFragment()).addToBackStack("").commit()
        }
    }

    private fun fillArrayList() {
        rbArray = arrayListOf(radio_quant_cream, radio_quant_milk, radio_quant_skim
                , radio_fat_cream, radio_fat_milk, radio_fat_skim)
        edtArray = arrayListOf(quant_cream, quant_milk, quant_skim
                , fat_cream, fat_milk, fat_skim)
        tvArray = arrayListOf(tr_quant_cream, tr_quant_milk, tr_quant_skim, tr_fat_cream
                , tr_fat_milk, tr_fat_skim)
    }

    private fun radioIsChecked(view: RadioButton) {
        canCalc = false
        for (i in 0 until rbArray.size) {
            rbArray[i].isChecked = false
        }
        for (i in 0 until rbArray.size) {
            if (rbArray[i] == view) {
                checkedButton = view
                edtArray[i].setText("")
                rbArray[i].isChecked = true
                check_text.setText(nameOfFields[i])
                tvArray[i].setBackgroundColor(Color.RED)
                edtArray[i].inputType = InputType.TYPE_NULL
                edtArray[i].setTextIsSelectable(false)
            } else {
                rbArray[i].isChecked = false
                tvArray[i].setBackgroundColor(Color.TRANSPARENT)
                edtArray[i].inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                edtArray[i].setTextIsSelectable(true)

            }
        }
        cantMoreThanZero()
        canCalc = true
    }

    private fun fillTriangle(text: EditText) {
        when (true) {
            text == quant_cream -> tr_quant_cream.setText("Ксл= ${text.text}")
            text == quant_milk -> tr_quant_milk.setText("Км= ${text.text}")
            text == quant_skim -> tr_quant_skim.setText("Кобм= ${text.text}")
            text == fat_cream -> tr_fat_cream.setText("Жсл= ${text.text}")
            text == fat_milk -> tr_fat_milk.setText("Жм= ${text.text}")
            text == fat_skim -> tr_fat_skim.setText("Жобм= ${text.text}")
        }

    }

    private fun createNameOfFields() {
        nameOfFields.add("- неизвестное значение - \"КОЛИЧЕСТВО СЛИВОК\" ")
        nameOfFields.add("- неизвестное значение - \"КОЛИЧЕСТВО МОЛОКА\" ")
        nameOfFields.add("- неизвестное значение - \"КОЛИЧЕСТВО ОБМ\" ")
        nameOfFields.add("- неизвестное значение - \"МДЖ СЛИВОК\" ")
        nameOfFields.add("- неизвестное значение - \"МДЖ МОЛОКА\" ")
        nameOfFields.add("- неизвестное значение - \"МДЖ ОБМ\" ")
    }

    private fun cantMoreThanZero() {
        canCalc = false
        try {
            for (i in 0 until edtArray.size) {
                edtArray[i].setBackgroundColor(Color.TRANSPARENT)
            }
            val color = Color.YELLOW
            when (true) {
                radio_quant_cream.isChecked || radio_fat_cream.isChecked -> {
                    quant_skim.setBackgroundColor(color)
                    quant_milk.setBackgroundColor(color)

                }

                radio_quant_milk.isChecked || radio_fat_milk.isChecked -> {
                    quant_skim.setBackgroundColor(color)
                    quant_cream.setBackgroundColor(color)
                }

                radio_quant_skim.isChecked || radio_fat_skim.isChecked -> {
                    quant_cream.setBackgroundColor(color)
                    quant_milk.setBackgroundColor(color)
                }

            }
        } catch (e: Exception) {
        }
        canCalc = true
    }

    private fun changeFieldToZero(text: EditText) {
        canCalc = false
        when (true) {
            checkedButton == radio_quant_cream || checkedButton == radio_fat_cream -> {
                if (text == quant_skim) {
                    quant_milk.setText("0")
                    addDoubleNumbers(quant_milk)
                }
                if (text == quant_milk) {
                    quant_skim.setText("0")
                    addDoubleNumbers(quant_skim)
                }
            }
            checkedButton == radio_quant_milk || checkedButton == radio_fat_milk -> {
                if (text == quant_skim) {
                    quant_cream.setText("0")
                    addDoubleNumbers(quant_cream)
                }
                if (text == quant_cream) {
                    quant_skim.setText("0")
                    addDoubleNumbers(quant_skim)
                }
            }
            checkedButton == radio_quant_skim || checkedButton == radio_fat_skim -> {
                if (text == quant_cream) {
                    quant_milk.setText("0")
                    addDoubleNumbers(quant_milk)
                }
                if (text == quant_milk) {
                    quant_cream.setText("0")
                    addDoubleNumbers(quant_cream)
                }
            }
        }
        canCalc = true
    }

    private fun addDoubleNumbers(editText: EditText) {
        when (true) {
            quant_cream.text.isNotEmpty() && editText == quant_cream -> {
                qCream = quant_cream.text.toString().toDouble()
                Log.d("TAG", "qCream$qCream")

            }
            quant_milk.text.isNotEmpty() && editText == quant_milk -> {
                qMilk = quant_milk.text.toString().toDouble()
                Log.d("TAG", "qMilk$qMilk")
            }
            quant_skim.text.isNotEmpty() && editText == quant_skim -> {
                qSkim = quant_skim.text.toString().toDouble()
                Log.d("TAG", "qSkim$qSkim")
            }
            fat_cream.text.isNotEmpty() && editText == fat_cream -> {
                fCream = fat_cream.text.toString().toDouble()
                Log.d("TAG", "fCream$fCream")
            }
            fat_milk.text.isNotEmpty() && editText == fat_milk -> {
                fMilk = fat_milk.text.toString().toDouble()
                Log.d("TAG", "fMilk$fMilk")
            }
            fat_skim.text.isNotEmpty() && editText == fat_skim -> {
                fSkim = fat_skim.text.toString().toDouble()
                Log.d("TAG", "fSkim$fSkim")
            }
        }
    }

    private fun calculateFields(editText: EditText) {
        canCalc = false
        addDoubleNumbers(editText)
        for (i in 0 until rbArray.size) {
            if (rbArray[i].isChecked) {
                edtArray[i].setText("")
                var count = 0
                for (j in 0 until edtArray.size) {
                    if (edtArray[j].text.isNotEmpty() && j !== i) {
                        count += 1
                    }
                }
                when (true) {
                    rbArray[i] == radio_quant_cream && count == 5 -> {
                        quant_cream.setText(calculate.quantCream(qMilk, qSkim, fCream, fMilk, fSkim))
                    }
                    rbArray[i] == radio_quant_milk && count == 5 -> {
                        quant_milk.setText(calculate.quantMilk(qCream, qSkim, fCream, fMilk, fSkim))
                    }
                    rbArray[i] == radio_quant_skim && count == 5 -> {
                        quant_skim.setText(calculate.quantSkim(qCream, qMilk, fCream, fMilk, fSkim))
                    }
                    rbArray[i] == radio_fat_cream && count == 5 -> {
                        fat_cream.setText(calculate.fatCream(qSkim, qMilk, qCream, fMilk, fSkim))
                    }
                    rbArray[i] == radio_fat_milk && count == 5 -> {
                        fat_milk.setText(calculate.fatMilk(qSkim, qMilk, qCream, fCream, fSkim))
                    }
                    rbArray[i] == radio_fat_skim && count == 5 -> {
                        fat_skim.setText(calculate.fatSkim(qMilk, qCream, qSkim, fCream, fMilk))
                    }
                }
            }
        }
        canCalc = true
    }
}