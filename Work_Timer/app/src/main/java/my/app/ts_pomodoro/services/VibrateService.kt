
package my.app.ts_pomodoro.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log

class VibrateService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startVibro()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startVibro() {
        val pattern = longArrayOf(0, 1000, 500, 1000, 500)

        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val canVibrate: Boolean = vibrator.hasVibrator()
        Log.d("TAG", "start VVVVVVVVibro")

        if (canVibrate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createWaveform(
                        pattern,
                        2
                    )
                )
            } else {
                vibrator.vibrate(pattern, 2)
            }
        }
    }

    private fun stopVibro (){
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val canVibrate: Boolean = vibrator.hasVibrator()
        if (canVibrate) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.cancel()
            } else {
                vibrator.cancel()
            }
        }
    }

    override fun onDestroy() {
        stopVibro()
        super.onDestroy()
    }

}