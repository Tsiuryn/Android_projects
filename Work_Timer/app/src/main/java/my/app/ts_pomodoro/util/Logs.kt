package my.app.ts_pomodoro.util

import android.content.Context
import android.util.Log
import android.widget.Toast


fun createLogs(message: String){
    val tag = "MY_TAG"
    Log.d(tag, message)
}

fun createToast (context: Context, message: String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}