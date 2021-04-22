package com.ts.alex.bestforecast.device.gps

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle

@SuppressLint("MissingPermission")
fun setUpLocationListener(context: Context, location: (Location) -> Unit) {
    val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    val locationListener: LocationListener = CustomLocationListener {
        if (it != null) {
            location(it)
        }
    }

    locationManager.requestLocationUpdates(
        LocationManager.GPS_PROVIDER,
        5_000,
        1000f,
        locationListener

    )
    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let { location(it) }
}


class CustomLocationListener(private val listener: (Location?) -> Unit) : LocationListener {

    override fun onLocationChanged(location: Location) {
        listener(location)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}
}