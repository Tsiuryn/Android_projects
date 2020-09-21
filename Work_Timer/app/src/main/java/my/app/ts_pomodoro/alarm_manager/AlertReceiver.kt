package my.app.ts_pomodoro.alarm_manager

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.PowerManager
import android.util.Log
import com.example.ts_pomodoro.R
import my.app.ts_pomodoro.constants.START_FINISH_NOTIFIC
import my.app.ts_pomodoro.fragments.TimerFragment
import my.app.ts_pomodoro.services.FinishService
import my.app.ts_pomodoro.services.VibrateService
import my.app.ts_pomodoro.util.PrefUtil

class AlertReceiver: BroadcastReceiver() {
    @SuppressLint("InvalidWakeLockTag")
    override fun onReceive(context: Context?, intent: Intent?) {
        val pm =
            context!!.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG")
        wl.acquire()

        if (context != null) {
            PrefUtil.setSecondsRemaining(0, context)
            if (PrefUtil.getTimerState(context) == TimerFragment.TimerState.WORK_RUNNING){
                PrefUtil.setTimerState(TimerFragment.TimerState.WORK_FINISHING, context)
            }else PrefUtil.setTimerState(TimerFragment.TimerState.WORK_RESETTING, context)

            Log.d("TAG", "from broadCast")
//            startNotification(context)
//            startVibro(context)
//            startSound(context)
            val myIntent = Intent(context, FinishService::class.java)
            myIntent.action = START_FINISH_NOTIFIC
            context.startService(myIntent)
        }
        wl.release()
    }
}