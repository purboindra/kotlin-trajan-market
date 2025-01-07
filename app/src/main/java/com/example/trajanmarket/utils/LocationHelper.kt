package com.example.trajanmarket.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.provider.Contacts.SettingsColumns
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil3.Uri
import com.example.trajanmarket.data.model.MarkerData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlinx.coroutines.tasks.await
import java.net.URI

private const val TAG = "LocationHelper"

class LocationHelper(private val context: Context) {

    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private lateinit var locationPermissionLauncher: ActivityResultLauncher<Array<String>>

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun setPermissionLauncher(launcher: ActivityResultLauncher<Array<String>>) {
        this.locationPermissionLauncher = launcher
    }

    fun requestPermission(activity: Activity) {
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[0])) {
            Toast.makeText(
                context,
                "Location permission is required to get current address",
                Toast.LENGTH_SHORT
            ).show()
            locationPermissionLauncher.launch(permissions)
        } else {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = android.net.Uri.fromParts("package", context.packageName, null)
            }

            context.startActivity(intent)
        }
    }

    fun requestLocationPermission(
        activity: Activity,
        locationPermissionLauncher: ActivityResultLauncher<Array<String>>
    ) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            showPermissionRationale(activity, locationPermissionLauncher)
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showPermissionRationale(
        activity: Activity,
        locationPermissionLauncher: ActivityResultLauncher<Array<String>>
    ) {
        AlertDialog.Builder(activity)
            .setTitle("Location Permission Required")
            .setMessage("This app requires location permission to function properly.")
            .setPositiveButton("Grant") { _, _ ->
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    suspend fun getCurrentAddress(): MarkerData? = withContext(Dispatchers.IO) {
        try {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@withContext null
            }

            val location: Location? = fusedLocationProviderClient.lastLocation.await()
            location?.let {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                val markerData = addresses?.get(0)?.let { marker ->
                    MarkerData(
                        longitude = location.longitude,
                        latitude = location.latitude,
                        title = marker.countryName
                    )
                }

                Log.d(TAG,"Marker data: ${markerData?.longitude}")

                return@withContext markerData
            }
        } catch (e: Exception) {
            Log.e("LocationHelper", "Error getting address: ${e.message}")
            return@withContext null
        }
    }
}