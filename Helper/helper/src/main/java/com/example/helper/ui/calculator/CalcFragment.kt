package com.example.helper.ui.calculator

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.helper.R
import com.example.helper.ui.calculator.my_database.database.CalculationsRepository
import com.example.helper.ui.calculator.my_database.local.CalculationsDataSource
import com.example.helper.ui.calculator.my_database.local.CalculationsDatabase
import com.example.helper.ui.calculator.my_database.model.Calculations
import com.example.helper.ui.calculator.reverse_polish_notation.ExpressionParser
import com.example.helper.ui.calculator.reverse_polish_notation.Ideone
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_caclulator.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CalcFragment : Fragment(), View.OnClickListener, View.OnLongClickListener {

    companion object {
        var isDbFragment = 1
    }
    private var expression = ArrayList<String>()
    private var listenerEditText = true
    private var position = 0
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var menu: Menu

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("TAG", "onCreateView")

        return inflater.inflate(R.layout.fragment_caclulator, container, false)
    }

    //показать/скрыть кнопку в меню
    private fun showItemMenu() {
        if (menu.findItem(R.id.dataBase) == null) return
        when (isDbFragment) {
            2 -> {
                menu.findItem(R.id.dataBase).isVisible = false
                menu.findItem(R.id.deleteBase).isVisible = true
            }
            1 -> {
                menu.findItem(R.id.dataBase).isVisible = true
                menu.findItem(R.id.deleteBase).isVisible = false
            }
            else -> {
                menu.findItem(R.id.dataBase).isVisible = false
                menu.findItem(R.id.deleteBase).isVisible = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (menu.findItem(R.id.deleteBase) == null) {
            inflater.inflate(R.menu.calculator_menu, menu)
            this.menu = menu
            showItemMenu()
        }
    }

    //открыть фрагмент с БД
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.dataBase -> {
                isDbFragment = 2
                showItemMenu()
                activity?.run{
                    this.supportFragmentManager?.beginTransaction()
                            .add(R.id.nav_host_fragment, DatabaseCalculatorFragment())
                            .addToBackStack("")
                            .commit()
                    return true
                }
            }
            R.id.deleteBase -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //скрыть все кнопки меню при отключении фрагмента
    override fun onPause() {
        super.onPause()
        Log.d("TAG", "onPauseCF")
        isDbFragment = 0
    }

    //вставка выражения из БД
    private fun fromDatabase() {
        val bundle = arguments
        if(bundle != null){
            val text = bundle.getString("KEY")
            editText.setText(text)
            editText.setSelection(text!!.length)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TAG", "onViewCreated")
        isDbFragment = 1
        compositeDisposable = CompositeDisposable()
        setHasOptionsMenu(true)
        editText.requestFocus()
        listenerForButtons()
        fromDatabase()
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (listenerEditText) {
                    calculate(editText.text.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    //основной метод для нажатия на клавишу
    private fun addSymbolsToEditText(symbol: String) {
        listenerEditText = false
        val pos = editText.selectionStart
        var newText = ""
        when (true) {
            symbol.matches(Regex("[0-9]")) -> newText = addNumber(pos, symbol)
            symbol.matches(Regex("[+\\-×÷]")) -> newText = addMainFunction(pos, symbol)
            symbol.equals("cos") || symbol.equals("sin") || symbol.equals("tan") -> newText = addTrigonometricFunction(pos, symbol)
            symbol.matches(Regex("[√ ( π]")) -> newText = addBracketsPi(pos, symbol)
            symbol.matches(Regex("[\\^ ) %]")) -> newText = percentCloseBracket(pos, symbol)
            symbol.matches(Regex("[.]")) -> newText = clickButtonDot(pos, symbol)
            symbol.equals("<-") -> newText = clickButtonBackspace(pos)
            symbol.equals("000") || symbol.equals("000000") || symbol.equals("000000000") -> newText = clickManyZero(pos, symbol)
            symbol.equals("=") -> {
                insertToDB(false)
                newText = clickButtonForResult()
            }
            symbol.equals(":)") -> newText = clickLongScCl(pos)
        }
        listenerEditText = true
        editText.setText(newText)
        editText.setSelection(position)
    }

    //метод добавления цифр
    private fun addNumber(position: Int, symbol: String): String {
        val sb = StringBuffer()
        if (editText.text.isNotEmpty()) {
            val text = editText.text.toString()
            sb.append(text)
            val lastSymbol = if (position != 0) text.substring(position - 1, position) else ""
            if (lastSymbol.matches(Regex("[% ) π]"))) {
                sb.insert(position, "×$symbol")
                this.position = position + symbol.length + 1
            } else {
                sb.insert(position, symbol)
                this.position = position + symbol.length
            }
        } else {
            sb.append(symbol)
            this.position = position + symbol.length
        }


        return digitNumbers(this.position, sb.toString())
    }

    //логика для добавления основных функций (+-*/)
    private fun addMainFunction(position: Int, symbol: String): String {
        var sb = StringBuffer()
        if (editText.text.isNotEmpty()) {
            val text = editText.text.toString()
            sb = StringBuffer(text)
            val lastSymbol = if (position != 0) text.substring(position - 1, position) else ""
            if (lastSymbol.matches(Regex("[0-9 \\π % )]"))) {
                sb.insert(position, symbol)
                this.position = position + symbol.length
            }
            if (lastSymbol == "(" && symbol == "-"){
                sb.insert(position, symbol)
                this.position = position + symbol.length
            }
        }

        return digitNumbers(this.position, sb.toString())
    }

    //взять все выражение в скобки
    private fun clickLongScCl(pos: Int): String {
        var sb = StringBuffer("")
        if (editText.text.isNotEmpty()) {
            val text = editText.text.toString()
            sb = StringBuffer(text)
            val lastSymbol = if (pos != 0) text.substring(pos - 1, pos) else ""
            val afterLastSymb = if (pos < sb.length) text.substring(pos, pos + 1) else ""
            if (lastSymbol.matches(Regex("[0-9 % π )]"))) {
                if (afterLastSymb.matches(Regex("[0-9 π c s t]"))) {
                    sb = sb.insert(pos, ")×")
                    position = pos + 1
                } else {
                    sb = sb.insert(pos, ')')
                }
                sb = sb.insert(0, '(')
                position += 2
            }
        }
        return sb.toString()
    }

    //добавление тригонометрических функций
    private fun addTrigonometricFunction(position: Int, symbol: String): String {
        var sb = StringBuffer("")
        if (editText.text.isNotEmpty()) {
            val text = editText.text.toString()
//            val text = editText.text.toString().replace(" ", "")
            sb = StringBuffer(text)
            val lastSymbol = if (position != 0) text.substring(position - 1, position) else ""
            if (lastSymbol.matches(Regex("[0-9 π )]"))) {
                sb.insert(position, "×$symbol")
                this.position = position + symbol.length + 1
            }
            if (lastSymbol.matches(Regex("[+ \\- × ÷ √ ^ (]"))) {
                sb.insert(position, symbol)
                this.position = position + symbol.length
            }
            if (lastSymbol == "") {
                sb.insert(position, symbol)
                this.position = position + symbol.length
            }
        } else {
            sb.append(symbol)
            this.position = position + symbol.length
        }

        return digitNumbers(this.position, sb.toString())
    }

    //добавление функций Пи скобка открывающаяся Корень
    private fun addBracketsPi(position: Int, symbol: String): String {
        var sb = StringBuffer("")
        if (editText.text.isNotEmpty()) {
            val text = editText.text.toString()
            sb = StringBuffer(text)
            val lastSymbol = if (position != 0) text.substring(position - 1, position) else ""
            if (lastSymbol.matches(Regex("[0-9]"))) {
                if (symbol == "√") {
                    sb.insert(position, symbol)
                    this.position = position + symbol.length
                } else {
                    sb.insert(position, "×$symbol")
                    this.position = position + symbol.length + 1
                }
            }
            if (lastSymbol.matches(Regex("[+ \\- × ÷ √ ^]"))) {
                sb.insert(position, "$symbol")
                this.position = position + symbol.length
            }
            if (lastSymbol.matches(Regex("[) π %]"))) {
                sb.insert(position, "×$symbol")
                this.position = position + symbol.length + 1
            }
            if (lastSymbol.matches(Regex("[s n n]"))) {
                sb.insert(position, "$symbol")
                this.position = position + symbol.length
            }
            if (lastSymbol == "") {
                sb.insert(position, symbol)
                this.position = position + symbol.length
            }
            if (lastSymbol == "(") {
                if (symbol == "(" || symbol == "√"){
                    sb.insert(position, symbol)
                    this.position = position + symbol.length
                }
            }

        } else {
            sb.append(symbol)
            this.position = position + symbol.length
        }
        return digitNumbers(this.position, sb.toString())
    }

    //Степеть, закрывающаяся скобка и процент
    private fun percentCloseBracket(position: Int, symbol: String): String {
        var sb = StringBuffer("")
        if (editText.text.isNotEmpty()) {
            val text = editText.text.toString()
            sb = StringBuffer(text)
            val lastSymbol = if (position != 0) text.substring(position - 1, position) else ""
            if (lastSymbol.matches(Regex("[0-9 % )]"))) {
                if (symbol == ")") {
                    val isCorrect = checkNumberOfBrackets(text, position)
                    if (isCorrect) {
                        sb.insert(position, symbol)
                        this.position = position + symbol.length
                    }
                } else if (symbol != ")") {
                    sb.insert(position, symbol)
                    this.position = position + symbol.length
                }
            }
        }
        return digitNumbers(this.position, sb.toString())
    }

    //добавление точки
    private fun clickButtonDot(position: Int, symbol: String): String {
        var sb = StringBuffer()
        if (editText.text.isNotEmpty()) {
            val text = editText.text.toString()
            sb = StringBuffer(text)
            val lastSymbol = if (position != 0) text.substring(position - 1, position) else ""
            if (lastSymbol.matches(Regex("[0-9]"))) {
                sb.insert(position, symbol)
                this.position = position + symbol.length
            }
        }
        return digitNumbers(this.position, sb.toString())
    }

    //удаление символа перед курсором
    private fun clickButtonBackspace(pos: Int): String {
        var text = ""
        if (editText.text.isNotEmpty() && pos == 0) {
            return editText.text.toString()
        }
        if (editText.text.isNotEmpty()) {
            text = editText.text.toString()
            val lastSymbol = if (position != 0) text.substring(position - 1, position) else ""
            if (lastSymbol == "n" || lastSymbol == "s") {
                if (pos < text.length) {
                    text = text.substring(0, pos - 3) + text.substring(pos, text.length)
                } else {
                    text = text.substring(0, pos - 3)
                }
                this.position = pos - 3
            } else if (lastSymbol != "") {
                if (pos == text.length) {

                    text = text.substring(0, pos - 1)
                    this.position = pos - 1

                } else {
                    text = text.substring(0, pos - 1) + text.substring(pos, text.length)
                    this.position = pos - 1
                }
            }
            if (lastSymbol == " ") {
                text = text.substring(0, pos - 2) + text.substring(pos, text.length)
                this.position = pos - 2
            }
        } else textResult.text = "0"
        if (text.isEmpty()) {
            textResult.text = "0"
        }
        return digitNumbers(this.position, text)
    }

    //добавление нескольких нулей
    private fun clickManyZero(pos: Int, symbol: String): String {
        var sb = StringBuffer()
        if (editText.text.isNotEmpty()) {
            val text = editText.text.toString()
            sb = StringBuffer(text)
            val lastSymbol = text.substring(pos - 1, pos)
            if (lastSymbol.matches(Regex("[0-9]"))) {
                sb.insert(pos, symbol)
                this.position = pos + symbol.length
            }
        }
        return digitNumbers(position, sb.toString())
    }

    // добавление пробелов в цифрах для отображения разрядности
    private fun digitNumbers(position: Int, text: String): String {
        val don = DigitsOfNumbers()
        val newText = don.composeText(text.replace(" ", ""))
        if (newText.length - text.length >= 1) {
            val count = newText.length - text.length
            this.position = position + count
        } else if (text.length - newText.length == 1) {
            this.position = position - 1
        }
        return newText
    }

    //нажатие на кнопку =
    private fun clickButtonForResult(): String {
        val text = calculate(editText.text.toString())
        position = text.length
        return text
    }

    //слушатель нажатия на кнупку
    override fun onClick(v: View?) {
        when (v) {
            editText -> closeKeyboard()
            button0 -> addSymbolsToEditText("0")
            button1 -> addSymbolsToEditText("1")
            button2 -> addSymbolsToEditText("2")
            button3 -> addSymbolsToEditText("3")
            button4 -> addSymbolsToEditText("4")
            button5 -> addSymbolsToEditText("5")
            button6 -> addSymbolsToEditText("6")
            button7 -> addSymbolsToEditText("7")
            button8 -> addSymbolsToEditText("8")
            button9 -> addSymbolsToEditText("9")
            buttonScOp -> addSymbolsToEditText("(")
            buttonScCl -> addSymbolsToEditText(")")
            buttonCancel -> {
                editText.setText("")
                textResult.text = "0"
                this.position = 0
            }
            buttonMinus -> addSymbolsToEditText("-")
            buttonPlus -> addSymbolsToEditText("+")
            buttonMultip -> addSymbolsToEditText("×")
            buttonDivis -> addSymbolsToEditText("÷")
            buttonRow -> addSymbolsToEditText("√")
            buttonPercent -> addSymbolsToEditText("%")
            buttonBackspace -> addSymbolsToEditText("<-")
            buttonDot -> addSymbolsToEditText(".")
            buttonResult -> addSymbolsToEditText("=")
        }
    }

    //добавление в Базу данный
    private fun insertToDB(showToast: Boolean){
        val db = CalculationsDatabase.getInstance(context)
        val repo = CalculationsRepository
                .getInstance(CalculationsDataSource
                        .getInstance(db.calculationsDAO()))
        val disposable = io.reactivex.Observable.create(object : ObservableOnSubscribe<Object>{
            override fun subscribe(emitter: ObservableEmitter<Object>) {
                if (editText.text.isNotEmpty()){
                    repo.insertCalculations(Calculations(editText.text.toString()
                            ,textResult.text.toString(), getCurrentDate(), "Нет описания"))
                    emitter.onComplete()
                }
            }

        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe()
        compositeDisposable.add(disposable)
        if(showToast)showToastAccept()
    }

    //показать тост об успешном добавлении в БД
    private fun showToastAccept(){
        val toast = Toast.makeText(context, "Выражение добавлено в БД!", Toast.LENGTH_SHORT)
        var view = toast.view
        val text = view.findViewById<TextView>(android.R.id.message)
        text.setTextColor(Color.RED)
        text.textSize = 16f
        toast.show()
    }

    //получение текущей даты и времени
    private fun getCurrentDate(): String{
        val date = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd, HH:mm:ss", Locale.getDefault())
        return dateFormat.format(date)
    }

    // для onViewCreate подписываю слушателя для кнопок и текста
    private fun listenerForButtons() {
        textView27.setText(Html.fromHtml("X<sup>n</sup>"))
        button0.setOnClickListener(this)
        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button6.setOnClickListener(this)
        button7.setOnClickListener(this)
        button8.setOnClickListener(this)
        button9.setOnClickListener(this)
        buttonPlus.setOnClickListener(this)
        buttonMinus.setOnClickListener(this)
        buttonDot.setOnClickListener(this)
        buttonDivis.setOnClickListener(this)
        buttonMultip.setOnClickListener(this)
        buttonScOp.setOnClickListener(this)
        buttonScCl.setOnClickListener(this)
        buttonCancel.setOnClickListener(this)
        buttonResult.setOnClickListener(this)
        buttonBackspace.setOnClickListener(this)
        buttonRow.setOnClickListener(this)
        buttonPercent.setOnClickListener(this)
        editText.setOnClickListener(this)

        button9.setOnLongClickListener(this)
        button6.setOnLongClickListener(this)
        button3.setOnLongClickListener(this)
        buttonDivis.setOnLongClickListener(this)
        buttonMultip.setOnLongClickListener(this)
        buttonMinus.setOnLongClickListener(this)
        buttonRow.setOnLongClickListener(this)
        buttonPlus.setOnLongClickListener(this)
        buttonScCl.setOnLongClickListener(this)
        buttonResult.setOnLongClickListener(this)


    }

    //закрывающая скобка ставится лишь в том случае, если перед ней есть
    //такое же количество открывающихся
    private fun checkNumberOfBrackets(text: String, pos: Int): Boolean {
        val array = text.toCharArray()
        var count = 0
        var count2 = 0
        var isCorrect = false
        for (i in 0 until pos) {
            if (array[i] == '(') {
                count++
            }
            if (array[i] == ')') {
                count2++
            }
        }
        if (count - count2 >= 1) {
            isCorrect = true
            }

        return isCorrect
    }

    //калькуляция результата
    private fun calculate(text: String): String {
        listenerEditText = false
        var result = ""
        try{
            expression = ExpressionParser.parse(correctText(text)) as ArrayList<String>
            result = Ideone.calc(expression).toString()
//            Log.d("GAT", "1 $result")
//            result = changeResult(result, 8)
//            Log.d("GAT", "2 $result")
////            result = changeResultDigits(result)
//            Log.d("GAT", "3 $result")
////            textResult.setText(result)
            textResult.setText("$result")
        }catch (e : Exception){
        }
        listenerEditText = true
        return result
    }

    //изменяем количество знаков после запятой
    private fun changeResult(text: String, qnt: Int): String {
        var sb = text
        if (sb.contains('.')) {
            var newText = sb.substring(sb.indexOf('.'), text.length)
            if (newText.length > qnt && !newText.contains('E')) {
                sb = sb.substring(0, sb.indexOf('.') + 1) + sb.substring(sb.indexOf('.') + 1, sb.indexOf('.') + 1 + qnt)
            }
            if (newText.contains('E')) {
                newText = newText.substring(newText.indexOf('.'), newText.indexOf('E'))
                if (newText.length > qnt) {
                    sb = sb.substring(0, sb.indexOf('.') + 1) +
                            sb.substring(sb.indexOf('.') + 1, sb.indexOf('.') + qnt) +
                            sb.substring(sb.indexOf('E'), sb.length)
                }
            }




            //для 3√125 = 5 - без показывает 4,9999999999
            val myCharArray = newText.substring(1, newText.length).toCharArray()
            var count  = 0
            if (myCharArray.size > 7){
                for (i in 0 until 8){
                    if (myCharArray[i] == '9'&& myCharArray[0] == '9'){
                        count++
                    }
                }
                if ( count > 7){
                    var numb = sb.substring(0, sb.indexOf('.')).toInt()
                    numb ++
                    sb = numb.toString()
                }
            }

        }
        val check = text.substring(text.length - 2)
        if (check.equals(".0")) {
            sb = text.substring(0, text.length - 2)
        }
        return sb
    }

    //добавление разрядности к результату
    private fun changeResultDigits(result: String): String{
        val don = DigitsOfNumbers()
        return don.composeText(result)
    }

    //корректировка текста перед его отправкой на вычисление
    private fun correctText(text: String): String {
        var newText = ""
        if (text.isNotEmpty()) {
            newText = text.replace("cos", "c")
                    .replace("sin", "s")
                    .replace("tan", "t")
                    .replace("π", "3.14159265")
                    .replace(" ", "")

            val text = changeSquare(newText)
            Log.d("TAG", "$newText")
            newText = text
            Log.d("TAG", "$newText")

        }
        return newText
    }

    //если перед корнем не стоит цифра -> добавляем 2
    private fun changeSquare(text: String): String {
        var sb = ""
        val list: ArrayList<Char> = text.toCharArray().toList() as ArrayList<Char>
        for (i in 0 until list.size) {
            if (list[i] == '√' && i != 0) {
                if (list[i - 1].toString().matches(Regex("[×÷+ \\- s n n ( )]"))) {
                    list.add(i, '2')
                }
            }
            if (list[i] == '√' && i == 0) {
                list.add(i, '2')
            }
        }
        for (i in 0 until list.size){
            sb += list[i]
        }

        return sb
    }

    //длинное нажатие на клавиши
    override fun onLongClick(v: View?): Boolean {
        when (v) {
            button9 -> addSymbolsToEditText("000000000")
            button6 -> addSymbolsToEditText("000000")
            button3 -> addSymbolsToEditText("000")
            buttonDivis -> addSymbolsToEditText("tan")
            buttonMultip -> addSymbolsToEditText("sin")
            buttonMinus -> addSymbolsToEditText("cos")
            buttonRow -> addSymbolsToEditText("^")
            buttonPlus -> addSymbolsToEditText("π")
            buttonScCl -> addSymbolsToEditText(":)")
            buttonResult -> insertToDB(true)
        }
        return true
    }

    //метод закрытия всплывающей клавиатуры
    private fun closeKeyboard() {
        val view = activity!!.currentFocus
        if (view != null) {
            val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    //показывать Toast для корня
    private fun showToast(text: String) {
        var toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        ((toast.view as LinearLayout).getChildAt(0) as TextView).gravity = Gravity.CENTER_HORIZONTAL
        toast.show()

    }
}