package com.ts.alex.kotlincleanproject.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

//установка разрядов
fun changingNumbers(numb: Int): String {
    val df = DecimalFormat()
    df.isGroupingUsed = true
    df.groupingSize = 3
    val decimalFormatSymbols = DecimalFormatSymbols()
    decimalFormatSymbols.decimalSeparator = '.'
    decimalFormatSymbols.groupingSeparator = '.'
    df.decimalFormatSymbols = decimalFormatSymbols
    return df.format(numb)
}
