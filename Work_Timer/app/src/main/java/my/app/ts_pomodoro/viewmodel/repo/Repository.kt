package my.app.ts_pomodoro.viewmodel.repo

import android.content.Context
import android.widget.Toast
import my.app.ts_pomodoro.classes.MyJobEntity
import my.app.ts_pomodoro.my_database.GoodJobDB
import my.app.ts_pomodoro.my_database.GoodJobEntity
import my.app.ts_pomodoro.util.createListMyJob
import my.app.ts_pomodoro.util.createLogs
import java.io.IOException

class Repository(val context: Context) {
    suspend fun getListFromDB(): List<MyJobEntity>{
        val list = ArrayList<MyJobEntity>()
        try {
            list.addAll(createListMyJob(GoodJobDB.getGoodJobDB(context)!!
                .getGoodJobDAO().getAllGoodJob() as ArrayList<GoodJobEntity>))
//            Log.d("TAG1", "getListFromDB")
//            Toast.makeText(context, "getList - ${list[0].time.size}", Toast.LENGTH_SHORT).show()
        }catch (e: IOException){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            createLogs(e.message.toString())
        }
        return list
    }

    suspend fun addGoodJobToDB (goodJobEntity: GoodJobEntity){
        try {
            GoodJobDB.getGoodJobDB(context)!!.getGoodJobDAO().addGoodJob(goodJobEntity)
//            Toast.makeText(context, "added to DB", Toast.LENGTH_SHORT).show()
        }catch (e: IOException){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }


}