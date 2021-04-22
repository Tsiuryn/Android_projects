package com.ts.alex.bestforecast.device.notification

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ts.alex.bestforecast.R


class Notification(private val context: Context) {
    fun start(channelId: String, title: String, message: String) {
        val notificationManageCompat = NotificationManagerCompat.from(context)

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_location)
            .build()
        notificationManageCompat.notify(1, notification)
    }


}
