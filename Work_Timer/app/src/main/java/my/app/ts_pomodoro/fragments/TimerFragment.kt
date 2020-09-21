package my.app.ts_pomodoro.fragments

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ts_pomodoro.R
import kotlinx.android.synthetic.main.fragment_timer.*
import my.app.ts_pomodoro.alarm_manager.AlertReceiver
import my.app.ts_pomodoro.constants.STOP_SOUND_NOTIFIC
import my.app.ts_pomodoro.my_database.GoodJobEntity
import my.app.ts_pomodoro.services.FinishService
import my.app.ts_pomodoro.services.TimerService
import my.app.ts_pomodoro.services.VibrateService
import my.app.ts_pomodoro.util.PrefUtil
import my.app.ts_pomodoro.util.createLogs
import my.app.ts_pomodoro.viewmodel.TimerViewModel
import my.app.ts_pomodoro.viewmodel.TimerViewModelFactory
import my.app.ts_pomodoro.viewmodel.repo.Repository
import java.text.SimpleDateFormat
import java.util.*

class TimerFragment : Fragment() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mediaPlayer = MediaPlayer.create(activity!!.applicationContext, R.raw.finish)
    }

    enum class TimerState {
        WORK_RESETTING, WORK_RUNNING, RELAX_PREPARING, RELAX_RUNNING, WORK_FINISHING, RELAX_FINISHING
    }

    private lateinit var timer: CountDownTimer
    private var timerState = TimerState.WORK_RESETTING
    private var timerLengthSeconds: Long = 0
    private var secondsRemaining: Long = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        main_start.setOnClickListener { v ->
            when (timerState) {
                TimerState.WORK_RESETTING -> {
                    setStateOfTheTimer(TimerState.WORK_RUNNING)
                    startTimer()
                    updateButtons()
                }
                TimerState.WORK_RUNNING -> {

                    timer.cancel()
                    setStateOfTheTimer(TimerState.WORK_RESETTING)
                    onTimerFinished()
                }
                TimerState.RELAX_RUNNING ->{
                    timer.cancel()
                    setStateOfTheTimer(TimerState.WORK_RESETTING)
                    onTimerFinished()
                }
            }
        }

    }

    private fun setStateOfTheTimer (timerSt: TimerState): TimerState {
        timerState = timerSt
        PrefUtil.setTimerState(timerState, requireContext())
        return timerState
    }

    private fun initTimer() {
        timerState = PrefUtil.getTimerState(requireContext())
        when (timerState) {
            TimerState.WORK_RUNNING -> {
                setSecondsRemaining()
                getRemainingTime()
                updateCountdownUI(secondsRemaining)
                updateButtons()
                startTimer()

            }
            TimerState.WORK_RESETTING -> {
                setLengthWorkTimer()
                updateCountdownUI(timerLengthSeconds)
                secondsRemaining = timerLengthSeconds
                updateButtons()
            }
            TimerState.WORK_FINISHING -> {
                dialogStopTimer()
            }
            TimerState.RELAX_PREPARING -> {
                setLengthRelaxTimer()
                updateCountdownUI(timerLengthSeconds)
                updateButtons()
                startTimer()
                setStateOfTheTimer(TimerState.RELAX_RUNNING)
            }

            TimerState.RELAX_RUNNING -> {
                setSecondsRemaining()
                getRemainingTime()
                updateCountdownUI(secondsRemaining)
                updateButtons()
                startTimer()

            }
            TimerState.RELAX_FINISHING -> {
                dialogRelaxTimer()

            }
        }
    }

    //получить оставшееся время после открытия приложения
    private fun getRemainingTime() {
        val finishedTime = PrefUtil.getCurrentTime(requireContext())
        val currentTime = Date(Calendar.getInstance().timeInMillis).time
        val remainingTime = finishedTime - currentTime
        if (remainingTime > 0) {
            secondsRemaining = remainingTime / 1000
        }

    }

    private fun setLengthWorkTimer() {
        val lengthInMinutes = PrefUtil.getTimerLength(requireContext())
        timerLengthSeconds = lengthInMinutes * 60L
//        timerLengthSeconds = lengthInMinutes                   //for testing
    }

    private fun setSecondsRemaining() {
        secondsRemaining = PrefUtil.getSecondsRemaining(requireContext())
    }

    private fun setLengthRelaxTimer() {
        val lengthInMinutes = PrefUtil.getLengthTimerRelax(requireContext())
        timerLengthSeconds = lengthInMinutes * 60L
//        timerLengthSeconds = lengthInMinutes                     //for testing
        secondsRemaining = timerLengthSeconds


    }

    private fun updateCountdownUI(secRem: Long) {
        val minutesUntilFinished = secRem / 60
        val secondsInMinuteUntilFinished = secRem - minutesUntilFinished * 60
        main_minutes.text = "${if (minutesUntilFinished.toString().length == 2)
            minutesUntilFinished.toString() else "0" + minutesUntilFinished.toString()}"
        main_seconds.text = "${if (secondsInMinuteUntilFinished.toString().length == 2)
            secondsInMinuteUntilFinished.toString() else "0" + secondsInMinuteUntilFinished.toString()}"

    }

    private fun updateButtons() {
        when (timerState) {
            TimerState.WORK_RUNNING -> {
                main_current.text = getString(R.string.Work)
                main_current.setTextColor(resources.getColor(R.color.text_work))
                main_seconds.setTextColor(resources.getColor(R.color.text_work))
                main_minutes.setTextColor(resources.getColor(R.color.text_work))
                main_start.setBackgroundColor(resources.getColor(R.color.background_work))
                main_start.setTextColor(resources.getColor(R.color.text_work))
                main_start.text = getString(R.string.reset)
            }
            TimerState.WORK_RESETTING -> {
                main_current.text = getString(R.string.Is_work)
                main_current.setTextColor(resources.getColor(R.color.text_work))
                main_seconds.setTextColor(resources.getColor(R.color.text_work))
                main_minutes.setTextColor(resources.getColor(R.color.text_work))
                main_start.setBackgroundColor(resources.getColor(R.color.background_work))
                main_start.setTextColor(resources.getColor(R.color.text_work))
                main_start.text = getString(R.string.start)
            }
            TimerState.RELAX_PREPARING, TimerState.RELAX_RUNNING -> {
                main_current.text = getString(R.string.Relax)
                main_current.setTextColor(resources.getColor(R.color.text_relax))
                main_seconds.setTextColor(resources.getColor(R.color.text_relax))
                main_minutes.setTextColor(resources.getColor(R.color.text_relax))
                main_start.setBackgroundColor(resources.getColor(R.color.background_relax))
                main_start.setTextColor(resources.getColor(R.color.text_relax))
                main_start.text = getString(R.string.skip)
            }
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() {

                if (timerState == TimerState.WORK_RUNNING){
                    dialogStopTimer()
                }else if (timerState == TimerState.RELAX_RUNNING){
                    dialogRelaxTimer()
                }
            }
            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI(secondsRemaining)
            }
        }.start()
    }

    private fun dialogStopTimer() {
        startSound()
        startVibro()
        insertToDB()
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.title_dialog_stop))
            .setMessage(getString(R.string.message_dialog_stop))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.negat_dialog_stop)) { dialog, which ->
                setStateOfTheTimer(TimerState.WORK_RESETTING)
                stopVibro()
                stopSound()
                onTimerFinished()
            }
            .setPositiveButton(getString(R.string.posit_dialog_stop)) { dialog, which ->
                setStateOfTheTimer(TimerState.RELAX_PREPARING)
                stopVibro()
                stopSound()
                onTimerFinished()
            }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun onTimerFinished() {
        if (timerState == TimerState.WORK_RESETTING){
            PrefUtil.setSecondsRemaining(timerLengthSeconds, requireContext())
            secondsRemaining = timerLengthSeconds
            initTimer()
        } else if (timerState == TimerState.RELAX_PREPARING) {
            PrefUtil.setSecondsRemaining(timerLengthSeconds, requireContext())
            secondsRemaining = timerLengthSeconds
            initTimer()
        }
    }

    private fun dialogRelaxTimer() {
        startSound()
        startVibro()
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.title_dialog_relax))
            .setMessage(getString(R.string.message_dialog_relax))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.posit_dialog_relax)) { dialog, which ->
                setStateOfTheTimer(TimerState.WORK_RESETTING)
                stopVibro()
                stopSound()
                onTimerFinished()
            }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun startVibro() {
        createLogs("startVibra")
        val intent = Intent(activity, VibrateService::class.java)
//        context!!.stopService(intent)
        context!!.startService(intent)
    }

    private fun stopVibro() {
        val intent = Intent(activity, VibrateService::class.java)
        context!!.stopService(intent)
    }

    private fun startSound() {
        createLogs("startSound")
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.start()
        }
    }

    private fun stopSound() {
        Log.d(PrefUtil.TAG, "TimerFragment -  stopSound")
        mediaPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        try {
            stopServiceTimer()
            destroyFinishService()
            initTimer()
            cancelAlarm()
//            clearFragmentBackStack()
        }catch (e: Exception){
        }

    }

    private fun startServiceTimer() {
        val intent = Intent(requireContext(), TimerService::class.java)
        if (timerState == TimerState.WORK_RUNNING|| timerState == TimerState.RELAX_RUNNING) {
            ContextCompat.startForegroundService(requireContext(), intent)
        }
    }

    private fun stopServiceTimer() {
        val intent = Intent(requireContext(), TimerService::class.java)
        context!!.stopService(intent)
    }

    override fun onPause() {
        super.onPause()
        createLogs("TimerFragment_onPause")
        try {
            timer.cancel()
        } catch (e: Exception) {
        }
        PrefUtil.setSecondsRemaining(secondsRemaining, requireContext())
        setStateOfTheTimer(timerState)
        PrefUtil.setCurrentTime(getFinishedTime(), requireContext())
        if (timerState == TimerState.WORK_RUNNING || timerState == TimerState.RELAX_RUNNING) {
            startAlarm(getFinishedTime())
            startServiceTimer()
        }
    }

    //время, когда таймер остановится
    private fun getFinishedTime(): Long {
        return Date(Calendar.getInstance().timeInMillis).time + secondsRemaining * 1000
    }

    //start AlarmManager
    // для Huawei Настройка приложения - Сведения энергопотребления - Запуск приложения:
    // автоматическое управление - откл. -> работа в фоновом режиме - вкл.
    @SuppressLint("InvalidWakeLockTag")
    private fun startAlarm(finishedTime: Long) {
        val alarmManager =
            context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlertReceiver::class.java)
        val pIntent = PendingIntent.getBroadcast(
            requireContext()
            , 1, intent, 0
        )
        if (finishedTime > Calendar.getInstance().timeInMillis) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, finishedTime, pIntent)
        }

    }

    private fun cancelAlarm() {
        val alarmManager =
            context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlertReceiver::class.java)
        val pIntent = PendingIntent.getBroadcast(
            requireContext()
            , 1, intent, 0
        )
        alarmManager.cancel(pIntent)
    }

    private fun getCurrentDate(): String {
        val date = Date()
        val time = date.time
//        val timeInMls = Calendar.getInstance().timeInMillis
        val form = SimpleDateFormat("dd.MM.yyyy")
        return form.format(time)
    }

    private fun getCurrentTime(): String {
        return Calendar.getInstance().timeInMillis.toString()
    }

    private fun destroyFinishService() {
        val intent = Intent(requireContext(), FinishService::class.java)
        activity!!.applicationContext.stopService(intent)
    }

    private fun insertToDB() {
        val vm = ViewModelProvider(
            this,
            TimerViewModelFactory(Repository(activity!!.applicationContext))
        ).get(TimerViewModel::class.java)

        val job = GoodJobEntity(
            getCurrentDate(),
            getCurrentTime(),
            PrefUtil.getTimerLength(requireContext()).toString()
        )
        vm.addJobToDB(job)

    }

    private fun clearFragmentBackStack() {
        val fm = activity?.supportFragmentManager
        for (i in 0 until fm!!.backStackEntryCount) {
            fm.popBackStack()
        }
    }

}