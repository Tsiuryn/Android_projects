package com.ts.alex.bestforecast.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog

internal class GPSPermission(private val activity: Activity) {

    fun checkPermission(listener: () -> Unit) {
        val permission =
            ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            getPermissionLocation()
        } else {
            listener.invoke()
        }
    }

    private fun getPermissionLocation() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            showDialog()
        } else requestPermissions()
    }

    private fun showDialog() {
        MaterialDialog(activity).apply {
            message(text = "To get a weather forecast, we need your current location. Allow to determine location?")
            positiveButton(text = "Allow", click = { requestPermissions()})
            negativeButton(text = "Cancel", click = {})
        }.show()
    }

    private fun requestPermissions() {
        val codePermission = 2
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), codePermission
        )
    }
}
