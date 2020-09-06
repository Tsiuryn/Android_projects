package alex.ts.coronavirusapp.adapter

import alex.ts.coronavirusapp.R
import alex.ts.coronavirusapp.model.Datum
import alex.ts.coronavirusapp.utils.changingNumbers
import alex.ts.coronavirusapp.viewmodel.model.Countries
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CountryListAdapter(private val itemList: List<Countries>) :
    RecyclerView.Adapter<CountryListAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        )
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val country = itemList[position]
        holder.confirmed.text = changingNumbers(country.totalRecovered)
        holder.country.text = country.country
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val confirmed = itemView.findViewById<TextView>(R.id.item_confirmed)
        val country = itemView.findViewById<TextView>(R.id.item_country)
    }
}
