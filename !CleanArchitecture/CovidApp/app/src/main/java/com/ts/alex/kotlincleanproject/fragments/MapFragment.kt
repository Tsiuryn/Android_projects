package com.ts.alex.kotlincleanproject.fragments

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_TYPE_MASK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.ts.alex.domain.model.Countries
import com.ts.alex.kotlincleanproject.MainViewModel
import com.ts.alex.kotlincleanproject.R
import com.ts.alex.kotlincleanproject.databinding.FragmentMapBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MapFragment : Fragment(), OnMapReadyCallback {
    private val vm: MainViewModel by sharedViewModel()
    private lateinit var map: GoogleMap

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
        vm.mapInfo.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            fillTheMap(it.listSortedCountries)
        })


    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            map = googleMap
            map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(53.903441, 27.557951)))

            changeMapMode()

            vm.updateMapStatus(onMapReady = true)
        }
    }

    private fun changeMapMode(){
        val currentNightMode = resources.configuration.uiMode and UI_MODE_NIGHT_MASK
        if(currentNightMode == Configuration.UI_MODE_NIGHT_YES){
            map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(), R.raw.map_style
                )
            )
        }
    }

    private fun chooseRadius(confirmed: Int): Double {
        var radius: Double = 0.0
        when {
            confirmed < 100000 -> {
                radius = 50 * 1000.0
            }
            confirmed in 100001..499999 -> {
                radius = 100 * 1000.0
            }
            confirmed >= 500000 -> {
                radius = 200 * 1000.0
            }
        }
        return radius
    }

    private fun chooseColor(confirmed: Int): Int {
        var color = 0x70D50000
        if (confirmed < 100000) {
            color = ContextCompat.getColor(
                requireContext(),
                R.color.theFirst
            )
        } else if (confirmed in 100001..499999) {
            color = ContextCompat.getColor(
                requireContext(),
                R.color.theSecond
            )
        } else if (confirmed >= 500000) {
            color = ContextCompat.getColor(
                requireContext(),
                R.color.theThird
            )
        }
        return color
    }

    private fun fillTheMap(listCountry: List<Countries>) {
        for (country in listCountry) {
            val lat = country.lat
            val lng = country.long
            val radius = chooseRadius(country.totalRecovered)
            val color = chooseColor(country.totalRecovered)
            drawMarkers(lat, lng, radius, color)
        }
    }

    private fun drawMarkers(lat: Double, lng: Double, radius: Double, color: Int) {
        val circleOptions = CircleOptions()
            .center(LatLng(lat, lng)).radius(radius)
            .fillColor(color).strokeColor(color)
        if(this::map.isInitialized){
            map.addCircle(circleOptions)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}