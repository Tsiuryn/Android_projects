package com.ts.alex.kotlincleanproject.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.ts.alex.domain.model.Countries
import com.ts.alex.kotlincleanproject.R
import com.ts.alex.kotlincleanproject.databinding.ItemCountryBinding
import com.ts.alex.kotlincleanproject.util.changingNumbers
import java.util.*
import java.util.zip.CheckedOutputStream
import kotlin.collections.ArrayList

class CountryListAdapter(private var itemList: List<Countries>) :
    RecyclerView.Adapter<CountryListAdapter.Holder>(), Filterable {
    var itemFilterList = ArrayList<Countries>()

    init {
        itemFilterList = itemList.toMutableList() as ArrayList<Countries>
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemPersonBinding = ItemCountryBinding.inflate(layoutInflater, parent, false)
        return Holder(itemPersonBinding)
    }

    override fun getItemCount(): Int = itemFilterList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val country = itemFilterList[position]
        holder.bind(country)
    }

    class Holder(private val itemViewBinding: ItemCountryBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(country: Countries) {
            itemViewBinding.vConfirmed.text =
                itemViewBinding.vConfirmed.context.getString(R.string.item_confirmed) + changingNumbers(country.totalConfirmed)
            itemViewBinding.vDeath.text = itemViewBinding.vDeath.context.getString(R.string.item_death) + changingNumbers(country.totalDeath)
            itemViewBinding.vRecovered.text =
                itemViewBinding.vRecovered.context.getString(R.string.item_recovered) + changingNumbers(country.totalRecovered)
            itemViewBinding.vTitle.text = country.country
        }
    }

    fun updateAdapter (list: List<Countries>){
        itemFilterList.clear()
        itemList = list
        itemFilterList = list as ArrayList<Countries>
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    itemFilterList = itemList as ArrayList<Countries>
                } else {
                    val resultList = ArrayList<Countries>()
                    for (country in itemList) {

                        if (country.country.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(country)
                        }
                    }
                    itemFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = itemFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                itemFilterList = results?.values as ArrayList<Countries>
                notifyDataSetChanged()
            }

        }
    }
}
