package com.ts.alex.bestforecast.ui.forecast.city_screen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ts.alex.bestforecast.R
import com.ts.alex.domain.model.City

class CityAdapter(private var itemList: ArrayList<City>, private val onClickListener:(city: City, isDeleted: Boolean) -> Unit) :
    RecyclerView.Adapter<CityAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val myJobList= LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
        val sch = Holder(myJobList)
        return sch
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val city = itemList[position]
        holder.cityName.text = city.name

        holder.deleteView.setOnClickListener {
            onClickListener(city, true)
        }
        holder.default.setOnClickListener {
            onClickListener(city, false)
        }
    }

    fun updateCityAdapter (list: ArrayList<City>){
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    class Holder (itemView: View): RecyclerView.ViewHolder(itemView){
        val cityName = itemView.findViewById<TextView>(R.id.vCityName)
        val deleteView = itemView.findViewById<ImageButton>(R.id.vDelete)
        val default = itemView.findViewById<ImageButton>(R.id.vDefault)
    }
}