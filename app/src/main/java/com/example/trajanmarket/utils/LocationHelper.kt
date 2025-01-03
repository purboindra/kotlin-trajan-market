package com.example.trajanmarket.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlinx.coroutines.tasks.await

private const val TAG = "LocationHelper"

class LocationHelper(private val context: Context) {

    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    suspend fun getCurrentAddress(): String? = withContext(Dispatchers.IO) {
        try {
            val location: Location? = fusedLocationProviderClient.lastLocation.await()
            location?.let {
                val geocoder = Geocoder(context, Locale.getDefault())
                val addresses = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    )
                } else {
                    geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1
                    ) ?: mutableListOf()
                }

                return@withContext addresses?.firstOrNull()?.getAddressLine(0)
            }
        } catch (e: Exception) {
            Log.e("LocationHelper", "Error getting address: ${e.message}")
        }
        return@withContext null
    }
}