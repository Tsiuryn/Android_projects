package com.ts.alex.kotlincleanproject.fragments

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ts.alex.domain.model.Countries
import com.ts.alex.kotlincleanproject.MainViewModel
import com.ts.alex.kotlincleanproject.R
import com.ts.alex.kotlincleanproject.adapter.CountryListAdapter
import com.ts.alex.kotlincleanproject.databinding.FragmentInfoBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class InfoFragment : Fragment() {
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CountryListAdapter
    private val viewModel: MainViewModel by sharedViewModel()

    private var searchView: SearchView? = null
    private var queryTextListener: SearchView.OnQueryTextListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        recyclerView = binding.listCountries
        adapter = CountryListAdapter(
            listOf<Countries>()
        )
        observeField()
    }

    @SuppressLint("SetTextI18n")
    private fun observeField() {
        try {
            viewModel.apply {
                getInformation.observe({ lifecycle }, {
                    createAdapter(it.listSortedCountries)
                    binding.confirmed.text = getString(R.string.info_total_confirmed) + it.confirmed
                    binding.totalDeaths.text = getString(R.string.info_total_death) + it.death
                    binding.recovered.text = getString(R.string.info_total_recovered) + it.recovered
                    binding.lastUpdate.text = it.lastUpdate
                })
            }
        } catch (e: IllegalStateException) {
        }

    }

    private fun createAdapter(list: List<Countries>) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.updateAdapter(list)
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)

        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView

        }
        if (searchView != null) {
            searchView!!.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            queryTextListener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if(newText != null){
                        adapter.filter.filter(newText)
                    }
                    return true
                }

            }
            searchView!!.setOnQueryTextListener(queryTextListener)
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> return false
        }
        searchView!!.setOnQueryTextListener(queryTextListener)
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}