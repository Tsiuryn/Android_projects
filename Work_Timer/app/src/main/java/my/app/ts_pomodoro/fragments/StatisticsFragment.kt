package my.app.ts_pomodoro.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ts_pomodoro.R
import kotlinx.android.synthetic.main.fragment_statistics.*
import my.app.ts_pomodoro.MainActivity
import my.app.ts_pomodoro.adapter.StatisticsAdapter
import my.app.ts_pomodoro.classes.MyJobEntity
import my.app.ts_pomodoro.viewmodel.StatisticsViewModel
import my.app.ts_pomodoro.viewmodel.StatisticsViewModelFactory
import my.app.ts_pomodoro.viewmodel.repo.Repository
import java.lang.ClassCastException

class StatisticsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StatisticsAdapter

    private var MIN_DISTANCE: Int = 0
    private var downX = 0f
    private var downY = 0f
    private var listJob = ArrayList<MyJobEntity>()

    private lateinit var statisticsCallback: StatisticsCallback

    interface StatisticsCallback{
        fun changeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById<RecyclerView>(R.id.list_goodJob)

        val dm = resources.displayMetrics
        MIN_DISTANCE = (120f * dm.densityDpi / 160f + 0.5).toInt()
        statistics_layout.setOnTouchListener { v, event -> onTouchEvent(event) }

        getListFromDB()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            statisticsCallback = context as StatisticsCallback
        }catch (e:ClassCastException){}
    }

    private fun getListFromDB(){
        val vm = ViewModelProvider(this,
            StatisticsViewModelFactory(Repository(activity!!.applicationContext))
        ).get(StatisticsViewModel::class.java)
        vm.listFromDB.observe(this, androidx.lifecycle.Observer {
                            if (it.isNotEmpty()){
                    listJob.clear()
                    listJob.addAll(it)
                    createAdapter(listJob)
                    text_statistics.setText("")
                }
        })
    }


    private fun createAdapter(list: List<MyJobEntity>) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = StatisticsAdapter(list as ArrayList<MyJobEntity>, object : StatisticsAdapter.Callback{
            override fun swiped() {
                showFragment2()
                statisticsCallback.changeFragment()
            }

        })
        recyclerView.adapter = adapter
    }

    private fun showFragment2( ) {
        activity!!.supportFragmentManager.beginTransaction().setCustomAnimations(
            R.anim.set_right_in, R.anim.set_left_out,
            R.anim.set_left_in, R.anim.set_right_out
        ).replace(R.id.container_fragment, TimerFragment()).commit()
        MainActivity.frag = 2
    }



    private fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return false
        }
        val mx = event.x
        val my = event.y
        Log.d("TAG", "onTouch statistic $my - y, $mx - x")

        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y

                Log.d("TAG", "$downX-downX  $downY- downY ")
                return true
            }
            MotionEvent.ACTION_UP -> {
                val upX = event!!.x
                val upY = event.y

                val deltaX = downX - upX
                val deltaY = downY - upY

                //горизонтальный свайп
                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    if (deltaX > 0) { //справа на лево
                       showFragment2()
                        statisticsCallback.changeFragment()
                        return true
                    }
                }
            }
        }
        return true
    }





}