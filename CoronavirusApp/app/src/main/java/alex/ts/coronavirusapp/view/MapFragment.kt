package alex.ts.coronavirusapp.view

import alex.ts.coronavirusapp.R
import alex.ts.coronavirusapp.model.Datum
import alex.ts.coronavirusapp.viewmodel.CountriesViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment: Fragment(), OnMapReadyCallback{
    private val vm: CountriesViewModel by activityViewModels<CountriesViewModel>()
    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            map = googleMap
        }
        map.let {
            it.moveCamera(CameraUpdateFactory.newLatLng(LatLng(53.903441, 27.557951)))
        }
        vm.listCountriesForMap.observe(this, androidx.lifecycle.Observer {
            fillTheMap(it)
        })
    }

    private fun chooseRadius (confirmed: Int): Double{
        var radius: Double = 0.0
        if (confirmed < 100000){
             radius = 50 * 1000.0
        }else if (confirmed> 100000 && confirmed < 500000){
            radius = 200 * 1000.0
        }else if (confirmed >= 500000){
            radius = 400 * 1000.0
        }
        return radius
    }

    private fun chooseColor (confirmed: Int): Int{
        var color = 0x70D50000
        if (confirmed < 100000){
            color = ContextCompat.getColor(requireContext(),
                R.color.theFirst
            )
        }else if (confirmed> 100000 && confirmed < 500000){
            color = ContextCompat.getColor(requireContext(),
                R.color.theSecond
            )
        }else if (confirmed >= 500000){
            color = ContextCompat.getColor(requireContext(),
                R.color.theThird
            )
        }
        return color
    }

    private fun fillTheMap (listCountry: List<Datum>){
        for (i in 0 until listCountry.size){
            val country = listCountry[i]
            val lat = country.lat
            val lng = country.long
            val radius = chooseRadius(country.confirmed)
            val color = chooseColor(country.confirmed)
            drawMarkers(lat, lng, radius, color)
        }
    }

    private fun drawMarkers(lat: Double, lng: Double, radius: Double, color: Int) {
        val circleOptions = CircleOptions()
            .center(LatLng(lat, lng)).radius(radius)
            .fillColor(color).strokeColor(color)
        map.addCircle(circleOptions)
    }
}