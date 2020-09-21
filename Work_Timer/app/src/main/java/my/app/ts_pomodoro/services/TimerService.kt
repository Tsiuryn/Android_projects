package my.app.ts_pomodoro.services

import android.app.PendingIntent
import android.app.Service
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.ts_pomodoro.R
import my.app.ts_pomodoro.MainActivity
import my.app.ts_pomodoro.fragments.TimerFragment
import my.app.ts_pomodoro.classes.App
import my.app.ts_pomodoro.util.PrefUtil
import java.text.SimpleDateFormat
import java.util.*


class TimerService : Service() {

    private var second = 0
    private lateinit var timerState: TimerFragment.TimerState
    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(PrefUtil.TAG, "TimerService - onStartCommand")
        timerState = PrefUtil.getTimerState(applicationContext)
        second = PrefUtil.getSecondsRemaining(applicationContext).toInt()
        startNotification()
        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag").apply {
                    acquire(second * 1000L)
                }
            }

        return START_NOT_STICKY

    }

    private fun startNotification() {
        val notificIntent = Intent(this, MainActivity::class.java)

        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(notificIntent)
        val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(this, App.CHANNEL_ID)
            .setContentTitle(getString(R.string.notif_title))
            .setContentText(getString(R.string.notif_message, getFinishedTime(second)))
            .setSmallIcon(R.drawable.ic_timer)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)

    }


    //получить текущее время в миллисекундах
    private fun getCurrentTime(): Long {
        return Date(Calendar.getInstance().timeInMillis).time
    }

    //Добавить к текущему времени количество секунд - возвратить в тексте
    private fun getFinishedTime(secondsRemaining: Int): String {
        val currentTime = getCurrentTime()
        val futureTime = currentTime + secondsRemaining * 1000
        val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return format.format(futureTime)
    }

    override fun onDestroy() {
        stopSelf()
        wakeLock.release()
        Log.d(PrefUtil.TAG, "TimerService - onDestroy")
        super.onDestroy()
    }
}