package alex.ts.coronavirusapp.view

import alex.ts.coronavirusapp.adapter.CountryListAdapter
import alex.ts.coronavirusapp.R
import alex.ts.coronavirusapp.viewmodel.CountriesViewModel
import alex.ts.coronavirusapp.viewmodel.model.Countries
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_info.*

class InfoFragment : Fragment() {
    val vm: CountriesViewModel by activityViewModels<CountriesViewModel>()
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recycler_view)
        observeField()
    }

    private fun observeField() {
        vm.dataForListInfo.observe(this, androidx.lifecycle.Observer {
            last_update.text = it.lastUpdate
            confirmed.text = it.confirmed
            total_deaths.text = it.death
            recovered.text = it.recovered
            it.listSortedCountries?.let { it1 -> createAdapter(it1) }
        })
    }

    private fun createAdapter(list: List<Countries>) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = CountryListAdapter(list)
        recyclerView.adapter = adapter
    }
}