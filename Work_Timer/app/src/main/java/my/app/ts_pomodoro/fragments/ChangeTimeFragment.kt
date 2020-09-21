package my.app.ts_pomodoro.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ts_pomodoro.R
import kotlinx.android.synthetic.main.fragment_change_time.*
import my.app.ts_pomodoro.util.PrefUtil
import java.lang.ClassCastException

class ChangeTimeFragment : Fragment() {

    private lateinit var changeTimeCallback: ChangeTimeCallback
    interface ChangeTimeCallback{
        fun timeWasChoose()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_change_time, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        relax_picker.maxValue = 10
        relax_picker.value = 5
        relax_picker.minValue = 3
        work_picker.maxValue = 45
        work_picker.value = 25
        work_picker.minValue = 15

        select.setOnClickListener {
            PrefUtil.setTimerLength(work_picker.value.toLong() , requireContext())
            PrefUtil.setLengthTimerRelax(relax_picker.value.toLong() , requireContext())
            clearFragmentBackStack()
            changeTimeCallback.timeWasChoose()

//            Toast.makeText(
//                context,
//                "Relax ${relax_picker.value}, Work ${work_picker.value}",
//                Toast.LENGTH_SHORT
//            ).show()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            changeTimeCallback = context as ChangeTimeCallback
        }catch (e: ClassCastException){}
    }

    private fun clearFragmentBackStack (){
        val fm = activity?.supportFragmentManager
        for (i in 0 until fm!!.backStackEntryCount){
            fm.popBackStack()
        }
    }




}