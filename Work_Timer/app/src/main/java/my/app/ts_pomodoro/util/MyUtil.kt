package my.app.ts_pomodoro.util

import my.app.ts_pomodoro.classes.MyJobEntity
import my.app.ts_pomodoro.my_database.GoodJobEntity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList



//парсинг листа из базы данных -> привидение к листу класса MyJob
fun createListMyJob(list: ArrayList<GoodJobEntity>): List<MyJobEntity> {
    var listJob = ArrayList<MyJobEntity>()
    if (list.isNotEmpty()) {
        val listTime = ArrayList<String>()
        listTime.add(changeDateFromGoodJob(list[0].finishTime.toLong(), list[0].workingTime.toLong()))
        listJob.add(
            MyJobEntity(
                list[0].date,
                listTime
            )
        )
        for (i in 0 until list.size) {
            val date = list[i].date
            val time = changeDateFromGoodJob(list[i].finishTime.toLong(), list[i].workingTime.toLong())
            var count = 0
            for (k in 0 until listJob.size) {
                if (listJob[k].date != date) {
                    count++
                }
            }
            if (count == listJob.size) {
                val newList = ArrayList<String>()
                newList.add(time)
                listJob.add(
                    MyJobEntity(
                        date,
                        newList
                    )
                )
            }
        }
        for (i in 0 until list.size) {
            val date = list[i].date
            val time = changeDateFromGoodJob(list[i].finishTime.toLong(), list[i].workingTime.toLong())
            for(j in 0 until listJob.size){
                if(listJob[j].date == date){
                    if (!listJob[j].time.contains(time)){
                        listJob[j].time.add(time)
                    }
                }
            }
        }


    }
    return listJob.reversed()
}

private fun changeDateFromGoodJob(finishTime: Long, workingTime: Long): String {
    val startTime = timeToString(finishTime - workingTime * 1000 * 60)
    val finishTime = timeToString(finishTime)
    return "$startTime - $finishTime"
}

private fun timeToString(time: Long): String {
    val form = SimpleDateFormat("HH:mm", Locale.getDefault())
    return form.format(time)
}