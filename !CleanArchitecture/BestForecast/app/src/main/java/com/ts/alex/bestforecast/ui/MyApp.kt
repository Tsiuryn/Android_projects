package com.ts.alex.bestforecast.ui

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.evernote.android.job.JobManager
import com.ts.alex.bestforecast.di.singleModule
import com.ts.alex.bestforecast.device.job.ForecastJobCreator
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApp : Application() {
    companion object{
        val CHANNEL_ID = "com.ts.alex.bestforecast.ui.CHANNEL_ID"
        lateinit var manager: NotificationManager
    }
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            androidLogger(Level.DEBUG)
            modules(listOf(singleModule))
        }
        createNotificationChannel()
        JobManager.create(this).addJobCreator(ForecastJobCreator())
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "Service channel",
                NotificationManager.IMPORTANCE_HIGH)

            manager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java)!!
            manager.createNotificationChannel(serviceChannel)
        }
    }
}