package my.app.ts_pomodoro.classes

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class App : Application() {

    companion object{
        val CHANNEL_ID = "com.example.ts_pomodoro.fragments.CHANNEL_ID"
        lateinit var manager: NotificationManager
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "Service channel",
            NotificationManager.IMPORTANCE_LOW)

            manager = getSystemService(NotificationManager::class.java)!!
            manager.createNotificationChannel(serviceChannel)
        }
    }
}