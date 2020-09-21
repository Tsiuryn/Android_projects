package my.app.ts_pomodoro


import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.ts_pomodoro.R
import kotlinx.android.synthetic.main.activity_main.*
import my.app.ts_pomodoro.fragments.ChangeTimeFragment
import my.app.ts_pomodoro.fragments.StatisticsFragment
import my.app.ts_pomodoro.fragments.TimerFragment
import my.app.ts_pomodoro.viewmodel.StatisticsViewModel


class MainActivity : AppCompatActivity(), StatisticsFragment.StatisticsCallback, View.OnClickListener, ChangeTimeFragment.ChangeTimeCallback {
    companion object {
        var frag = 1



    }

    private lateinit var vm: StatisticsViewModel
    private var MIN_DISTANCE: Int = 0
    private var downX = 0f
    private var downY = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        val dm = resources.displayMetrics
        MIN_DISTANCE = (120f * dm.densityDpi / 160f + 0.5).toInt()
        statistic_button.setOnClickListener(this)
        timer_button.setOnClickListener(this)
        change_time_button.setOnClickListener(this)
        showFragment2(true)
    }

    private fun showFragment1() {
        supportFragmentManager.beginTransaction().setCustomAnimations(
            R.anim.set_left_in, R.anim.set_right_out,
            R.anim.set_right_in, R.anim.set_left_out
        ).replace(R.id.container_fragment, StatisticsFragment()).commit()
        frag = 1
        changeButton()
    }

    private fun showFragment2(isFromTheFirst: Boolean) {
        if (isFromTheFirst) {
            supportFragmentManager.beginTransaction().setCustomAnimations(
                R.anim.set_right_in, R.anim.set_left_out,
                R.anim.set_left_in, R.anim.set_right_out
            ).replace(R.id.container_fragment, TimerFragment()).commit()
        } else supportFragmentManager.beginTransaction().setCustomAnimations(
            R.anim.set_left_in, R.anim.set_right_out,
            R.anim.set_right_in, R.anim.set_left_out
        ).replace(R.id.container_fragment, TimerFragment()).commit()

        frag = 2
        changeButton()
    }

    private fun showFragment3() {
        supportFragmentManager.beginTransaction().setCustomAnimations(
            R.anim.set_right_in, R.anim.set_left_out,
            R.anim.set_left_in, R.anim.set_right_out
        ).replace(R.id.container_fragment, ChangeTimeFragment()).commit()
        frag = 3
        changeButton()
    }

    private fun changeButton() {
        when (frag) {
            1 -> {
                isEnableStatisticsButton(true)
                isEnableTimerButton(false)
                isEnableChangingTimeButton(false)
            }
            2 -> {
                isEnableStatisticsButton(false)
                isEnableTimerButton(true)
                isEnableChangingTimeButton(false)
            }
            3 -> {
                isEnableStatisticsButton(false)
                isEnableTimerButton(false)
                isEnableChangingTimeButton(true)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return false
        }
        val mx = event.x
        val my = event.y

        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                return true
            }
            MotionEvent.ACTION_UP -> {
                val upX = event!!.x
                val upY = event.y

                val deltaX = downX - upX
                val deltaY = downY - upY

                //горизонтальный свайп
                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    Log.d("TAG", "main act $MIN_DISTANCE")
                    if (deltaX < 0) { //слева на право
                        when (frag) {
                            2 -> {
                                this.showFragment1()
                                return true
                            }
                            3 -> {
                                this.showFragment2(false)
                                return true
                            }

                        }
                        return false
                    }
                    if (deltaX > 0) { //справа на лево
                        when (frag) {
                            1 -> {
                                this.showFragment2(true)
                                return true
                            }
                            2 -> {
                                this.showFragment3()
                                return true
                            }

                        }
                        return false
                    }
                }
            }
        }
        return true
    }

    private fun isEnableStatisticsButton(isEnable: Boolean) {
        if (isEnable) {
            statistic_button.setImageResource(R.drawable.information_enable)
            statistic_view.background = getDrawable(R.color.enable_fragment)
        } else {
            statistic_button.setImageResource(R.drawable.information)
            statistic_view.background = getDrawable(R.color.not_enable_fragment)
        }
    }

    private fun isEnableTimerButton(isEnable: Boolean) {
        if (isEnable) {
            timer_button.setImageResource(R.drawable.ic_timer_enable)
            timer_view.background = getDrawable(R.color.enable_fragment)
        } else {
            timer_button.setImageResource(R.drawable.ic_timer)
            timer_view.background = getDrawable(R.color.not_enable_fragment)
        }
    }

    private fun isEnableChangingTimeButton(isEnable: Boolean) {
        if (isEnable) {
            change_time_button.setImageResource(R.drawable.changing_enable)
            change_time_view.background = getDrawable(R.color.enable_fragment)
        } else {
            change_time_button.setImageResource(R.drawable.changing)
            change_time_view.background = getDrawable(R.color.not_enable_fragment)
        }
    }

    override fun changeFragment() {
        changeButton()
    }

    override fun onClick(v: View?) {
        when(v){
            statistic_button ->{
                if (frag == 2 || frag == 3){
                    showFragment1()
                }

            }
            timer_button -> {
                if (frag == 1){
                    showFragment2(true)
                }else if (frag == 3){
                    showFragment2(false)
                }
            }
            change_time_button ->{
                if (frag == 1 || frag == 2){
                    showFragment3()
                }
            }
        }
    }

    override fun timeWasChoose() {
        showFragment2(false)
    }


}
