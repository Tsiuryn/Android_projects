package my.app.ts_pomodoro.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ts_pomodoro.R
import my.app.ts_pomodoro.classes.MyJobEntity


class StatisticsAdapter(private var itemList: ArrayList<MyJobEntity>, private val myCallback: Callback) :
    RecyclerView.Adapter<StatisticsAdapter.Holder>() {

    private var MIN_DISTANCE: Int = 0
    private var downX = 0f
    private var downY = 0f

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_adapter, parent, false))
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val myJob = itemList[position]
        holder.date.setText(myJob.date)
        holder.time.setText(getTime(myJob))
        holder.itemView.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                return onTouchEvent(event)
                return true
            }
        })
    }

    private fun getTime(myJob: MyJobEntity): String {
        val list = myJob.time
        var time = ""
        for (j in 0 until list.size){
            if (j == 0){
                time = "${j+1}. ${list[j]}"
            }
            else{
                time = "$time \n${j+1}. ${list[j]}"
            }
        }
        return time
    }

//    fun updateAdapter(myList: List<MyJobEntity>) {
//        itemList.clear()
//        itemList.addAll(myList)
//
//        notifyDataSetChanged()
//    }

//    private fun onTouchEvent(event: MotionEvent?): Boolean {
//
//        if (event == null) {
//            return false
//        }
//        val mx = event.x
//        val my = event.y
//        Log.d("TAG", "adapter statistic $my - y, $mx - x")

//        when (event!!.action) {
//            MotionEvent.ACTION_DOWN -> {
//                downX = event.x
//                downY = event.y
//
//
//                return true
//            }
//            MotionEvent.ACTION_UP -> {
//                val upX = event!!.x
//                val upY = event.y
//
//                val deltaX = downX - upX
//                val deltaY = downY - upY
//                Log.d("TAG", "adapter ${Math.abs(deltaX)}")
//
//                //горизонтальный свайп
//                if (Math.abs(deltaX) > 100) {
//                    if (deltaX > 0) { //справа на лево
//                        myCallback.swiped()
//                        return true
//                    }
//                }
//            }
//        }
//        return true
//    }

    class Holder (itemView: View): RecyclerView.ViewHolder(itemView){
        val date = itemView.findViewById<TextView>(R.id.date_goodJob)
        val time = itemView.findViewById<TextView>(R.id.time_goodJob)
    }

    interface Callback{
        fun swiped()
    }
}
