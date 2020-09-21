package my.app.ts_pomodoro.classes

import android.content.Context
import android.media.MediaPlayer
import com.example.ts_pomodoro.R
import my.app.ts_pomodoro.util.createLogs

abstract class MyPlayer{

    companion object{
        private var INSTANCE: MediaPlayer? = null

        fun createMyPlayer (context: Context): MediaPlayer?{
            if (INSTANCE == null){
                synchronized(MyPlayer::class.java){
                    INSTANCE = MediaPlayer.create(context, R.raw.finish)
                    createLogs("getMyPlayer")
                }



            }
            return INSTANCE
        }

//        fun stopPlayer (context: Context){
//            getMyPlayer(context)
//            INSTANCE!!.stop()
//        }
//
//        fun startPlayer (context: Context){
//            getMyPlayer(context)
//            INSTANCE!!.start()
//        }
    }
}