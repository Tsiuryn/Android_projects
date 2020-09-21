package my.app.ts_pomodoro.services

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.ts_pomodoro.R
import my.app.ts_pomodoro.MainActivity
import my.app.ts_pomodoro.classes.App
import my.app.ts_pomodoro.constants.START_FINISH_NOTIFIC
import my.app.ts_pomodoro.constants.STOP_SOUND_NOTIFIC
import my.app.ts_pomodoro.fragments.TimerFragment
import my.app.ts_pomodoro.util.PrefUtil
import my.app.ts_pomodoro.util.createLogs

class FinishService: Service(){
    private lateinit var mediaPlayer: MediaPlayer

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent!!.action == START_FINISH_NOTIFIC){
            startNotification()
            mediaPlayer = MediaPlayer.create(applicationContext, R.raw.finish)
            startVibro()
            startSound()
        }else if(intent.action == STOP_SOUND_NOTIFIC){
            changeTimerState()
            stopSound()
            stopVibro()
        }

        return START_NOT_STICKY
    }

    private fun changeTimerState (){
        PrefUtil.setTimerState(TimerFragment.TimerState.WORK_RESETTING, this)
    }

    private fun startNotification() {
        createLogs("startNotific - service")

        val stopSoundIntent = Intent(this, FinishService::class.java)
        stopSoundIntent.setAction(STOP_SOUND_NOTIFIC)
        val pIntentOk = PendingIntent.getService(this, 0, stopSoundIntent, 0)


        val notificIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificIntent, 0
        )
        val notificationManageCompat = NotificationManagerCompat.from(this)
        val notification =
            NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setContentTitle(getString(R.string.stop_notif_title))
                .setContentText(getString(R.string.stop_notif_message))
                .setSmallIcon(R.drawable.ic_timer)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_stop, "OK", pIntentOk)
                .build()
        notificationManageCompat.notify(1, notification)
    }

    private fun startVibro() {
        val intent = Intent(applicationContext, VibrateService::class.java)
        applicationContext!!.stopService(intent)
        applicationContext!!.startService(intent)
    }

    private fun stopVibro() {
        val intent = Intent(applicationContext, VibrateService::class.java)
        applicationContext!!.stopService(intent)
    }
    private fun startSound() {
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.start()
        }
    }

    private fun stopSound() {
        mediaPlayer.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        createLogs("FinishService - onDestroy")
        stopSound()
    }
}