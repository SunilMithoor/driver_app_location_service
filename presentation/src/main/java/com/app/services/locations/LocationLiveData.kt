package com.app.services.locations

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import timber.log.Timber
import java.util.concurrent.TimeUnit


/**
 * Constants Values
 */
private const val INTERVAL = 10000L //10 seconds
private const val FASTEST_INTERVAL = 5000L //5 seconds
private const val SMALLEST_DISPLACEMENT_METERS = 10F //10 metres


class LocationLiveData(context: Context) : MutableLiveData<Location>() {

    private var fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)


    /**
     * Sets the value. If there are active observers, the value will be dispatched to them.
     *
     * This method must be called from the main thread. If you need set a value from a background
     * thread, you can use postValue(Object)
     *
     */
//    private fun setLocationData(location: Location) {
//        value = location
//    }

    /**
     * Static object of location request
     */
    companion object {
        val locationRequest: LocationRequest = LocationRequest.create()
            .apply {
                interval = INTERVAL
                fastestInterval = FASTEST_INTERVAL
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                smallestDisplacement = SMALLEST_DISPLACEMENT_METERS
            }
    }


    /**
     * Called when the number of active observers change to 1 from 0.
     * This callback can be used to know that this LiveData is being used thus should be kept
     * up to date.
     */
    override fun onInactive() {
        super.onInactive()
        Timber.d("Location Inactive")
        stopLocationUpdates()
    }


    /**
     * Called when the number of active observers change from 1 to 0.
     *
     * This does not mean that there are no observers left, there may still be observers but their
     * lifecycle states aren't STARTED or RESUMED
     * (like an Activity in the back stack).
     *
     * You can check if there are observers via hasObservers() method
     */
    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        Timber.d("Location Active")
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.also {
                    postValue(it)
                }
            }
        startLocationUpdates()
    }

    /**
     * Callback that triggers on location updates available
     */
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult ?: return
            for (location in locationResult.locations) {
                postValue(location)
            }
        }
    }

    /**
     * Initiate Location Updates using Fused Location Provider and
     * attaching callback to listen location updates
     */

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

}