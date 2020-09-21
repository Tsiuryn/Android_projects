package my.app.ts_pomodoro.util

import android.content.Context
import android.util.Log
import my.app.ts_pomodoro.fragments.TimerFragment

class PrefUtil {
    companion object{
        const val TAG = "MY_TAG"
        private const val PREFERENCES = "com.example.ts_pomodoro.fragments.util.preferences"

        private const val TIMER_LENGTH_ID = "com.example.ts_pomodoro.fragments.util.timer_length"
        fun getTimerLength(context: Context): Long{
            val preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            val time = preferences.getLong(TIMER_LENGTH_ID, 10)
            Log.d(TAG, "PrefUtil - getTimerLength - $time")
            return time
        }

        fun setTimerLength(seconds: Long, context: Context){
            val editor = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            editor.edit().putLong(TIMER_LENGTH_ID, seconds).apply()
            Log.d(TAG, "PrefUtil - setTimerLength")

        }

        private const val CURRENT_tIME_ID = "com.example.ts_pomodoro.fragments.util.current_time"
        fun getCurrentTime (context: Context): Long{
            val preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            return preferences.getLong(CURRENT_tIME_ID, 0)
        }

        fun setCurrentTime (mlSeconds: Long, context: Context){
            val editor = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            editor.edit().putLong(CURRENT_tIME_ID, mlSeconds).apply()
        }


        private const val TIMER_STATE_ID = "com.example.ts_pomodoro.fragments.util.timer_state"
        fun getTimerState(context: Context): TimerFragment.TimerState{
            val preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            val ordinal = preferences.getInt(TIMER_STATE_ID, 0)

            Log.d(TAG, "PrefUtil - getTimerState")

            return TimerFragment.TimerState.values()[ordinal]
        }

        fun setTimerState(state: TimerFragment.TimerState, context: Context){
            val editor = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            val ordinal = state.ordinal
            editor.edit().putInt(TIMER_STATE_ID, ordinal).apply()
            Log.d(TAG, "PrefUtil - setTimerState")
        }


        private const val SECONDS_REMAINING_ID = "com.example.ts_pomodoro.fragments.util.seconds_remaining"
        fun getSecondsRemaining(context: Context): Long{
            val preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            Log.d(TAG, "PrefUtil - getSecondsRemaining")

            return preferences.getLong(SECONDS_REMAINING_ID, 1)
        }

        fun setSecondsRemaining(seconds: Long, context: Context){
            val editor = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            editor.edit().putLong(SECONDS_REMAINING_ID, seconds).apply()
            Log.d(TAG, "PrefUtil - setSecondsRemaining")
        }

        private const val RELAX_SECONDS_REMAINING_ID = "com.example.ts_pomodoro.fragments.util.relax_seconds_remaining"

        fun getLengthTimerRelax(context: Context): Long{
            val preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            Log.d(TAG, "PrefUtil - getSecondsRemaining")

            return preferences.getLong(RELAX_SECONDS_REMAINING_ID, 5)
        }

        fun setLengthTimerRelax (seconds: Long, context: Context){
            val editor = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            editor.edit().putLong(RELAX_SECONDS_REMAINING_ID, seconds).apply()
            Log.d(TAG, "PrefUtil - setSecondsRemaining")
        }
    }
}