package alex.ts.coronavirusapp.utils

import alex.ts.coronavirusapp.model.Datum
import alex.ts.coronavirusapp.viewmodel.model.Countries
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


fun changeTime(mls: Long?): String {
    val form = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
    return form.format(mls)
}

//изменение листа, убираем повторяющиеся страны и суммируем зараженных людей
fun sortListForInfoFragment(list: ArrayList<Datum>): ArrayList<Countries> {
    val map = list.groupBy ({ it.country},{it.confirmed})
    val myList = map.toList()
    var sortedList = ArrayList<Countries>()
    for (i in 0 until myList.size){
        val country = myList[i].first
        var count = 0
        for(j in 0 until myList[i].second.size){
            count += myList[i].second[j]
        }
        sortedList.add(Countries(country, count))
    }
    sortedList.sortBy { it.country }
    return sortedList
}

//установка разрядов
fun changingNumbers(numb: Int?): String {
    val df = DecimalFormat()
    df.isGroupingUsed = true
    df.groupingSize = 3
    val decimalFormatSymbols = DecimalFormatSymbols()
    decimalFormatSymbols.decimalSeparator = '.'
    decimalFormatSymbols.groupingSeparator = '.'
    df.decimalFormatSymbols = decimalFormatSymbols
    return df.format(numb)
}

