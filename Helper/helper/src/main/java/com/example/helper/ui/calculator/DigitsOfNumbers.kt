package com.example.helper.ui.calculator

class DigitsOfNumbers {
    private fun addSpaces(text: String) : String {
        val array = text.toCharArray()
        var list = ArrayList<Char>()
        var count = 0
        var newText = ""
        var numb = -1
        for (j in array.size - 1 downTo 0) {
            if (array[j] == '.') {
                numb = j
            }
        }
        for (i in 0 until array.size){
            list.add(array[i])
        }
        val size = if (numb != -1) numb else list.size - 1

        for (i in size downTo 0) {
            if (array[i].toString().matches(Regex("[0-9]"))) {
                count += 1
                if (count % 3 == 0 && i != 0) {
                    list.add(i, ' ')
                    count = 0
                }
            }
        }
        for (i in 0 until list.size) {
            newText += list[i]
        }
        return newText
    }

    private fun getNumbers (text: String): List<String> {
        return text.split(Regex("[+ \\- × ÷ ( ) √ ^ %]"))
    }

    fun composeText (text: String): String{
        val array = getNumbers(text)
        var newText = text
        for (i in 0 until array.size){
            if (array[i].isNotEmpty()){
                val replacement = addSpaces(array[i])
                newText = newText.replace(array[i], replacement, true)
            }
        }
        return newText
    }
}