package com.ts.alex.bestforecast.ui.forecast.city_screen

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.ts.alex.bestforecast.R
import com.ts.alex.bestforecast.databinding.FragmentCityBinding
import com.ts.alex.bestforecast.databinding.FragmentForecastMainBinding
import com.ts.alex.bestforecast.ui.forecast.city_screen.adapter.CityAdapter
import com.ts.alex.bestforecast.ui.forecast.main_screen.ForecastMainViewModel
import com.ts.alex.domain.model.City
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel


class CityFragment : Fragment() {
    private val viewModel by viewModel<CityViewModel>()
    private lateinit var binding: FragmentCityBinding
    private lateinit var navController: NavController

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: CityAdapter

    private var city = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_city,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        navController = Navigation.findNavController(view)
        recycler = binding.vRecycler
        onBackPress()
        setUpAdapter()
        observe()
        viewModel.getListCity()

    }

    private fun setUpAdapter() {
        recycler.layoutManager = LinearLayoutManager(context)
        adapter = CityAdapter(ArrayList<City>()){city, isDeleted ->
            if(isDeleted){
                viewModel.deleteCity(city)
            }else {
                showDialog(city)
            }

        }
        val dividerItemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        recycler.addItemDecoration(dividerItemDecoration)
        recycler.adapter = adapter
    }

    private fun showDialog(city: City) {
        MaterialDialog(requireContext()).show {
            message (res = R.string.city_dial_message)
            positiveButton(text = "Ok"){
                viewModel.saveCityInPreferences(city.name)
                val action = CityFragmentDirections.actionCityFragmentToForecastMainFragment()
                navController.navigate(action)
            }
            negativeButton(text = "Cancel"){
                dismiss()
            }
        }
    }

    private fun observe() {
        lifecycleScope.launchWhenStarted {
            viewModel.weatherByCity.collect {
                if(it){
                    viewModel.addCity(city)
                    binding.vProgress.visibility = View.GONE
                    Toast.makeText(requireContext(), getString(R.string.city_added), Toast.LENGTH_SHORT).show()
                }else{
                    binding.vProgress.visibility = View.GONE
                    Toast.makeText(requireContext(), getString(R.string.city_find), Toast.LENGTH_SHORT).show()

                }

            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.listCity.collect {
                if(it.isNotEmpty()) binding.vMessage.visibility = View.GONE
                adapter.updateCityAdapter(it as ArrayList<City>)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.clickAdd.collect {
                if(it){
                    city = binding.vChooseCity.editText!!.text.toString()
                    if(city.isNotEmpty()){
                        binding.vProgress.visibility = View.VISIBLE
                        viewModel.getWeatherByCity(city)
                    }else{
                        Toast.makeText(requireContext(), getString(R.string.city_empty), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun onBackPress() {
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val action = CityFragmentDirections.actionCityFragmentToForecastMainFragment()
                navController.navigate(action)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(), callback)
    }
}