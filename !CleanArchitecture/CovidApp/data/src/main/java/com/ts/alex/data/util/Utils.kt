package com.ts.alex.data.util

import com.ts.alex.data.model.Datum
import com.ts.alex.domain.model.Countries
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
fun sortListForInfoFragment(list: ArrayList<Datum>): List<Countries> {
    val map = list.groupBy ({ it.country},{it})
    val myList = map.toList()
    val sortedList = ArrayList<Countries>()
    for (i in myList.indices){
        val country = myList[i].first
        var recovered = 0
        var confirmed = 0
        var death = 0
        var lat = 0.0
        var long = 0.0
        for(element in myList[i].second){
            recovered += element.recovered
            confirmed += element.confirmed
            death += element.deaths
            lat = element.lat
            long = element.long
        }
        sortedList.add(Countries(
            country = country,
            totalRecovered = recovered,
            totalConfirmed = confirmed,
            totalDeath = death,
            lat = lat,
            long = long
        ))
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

