//package com.app.services.locations
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.location.Location
//import android.os.Looper
//import android.widget.Toast
//import androidx.lifecycle.MutableLiveData
//import com.google.android.gms.common.api.ApiException
//import com.google.android.gms.common.api.GoogleApiClient
//import com.google.android.gms.location.*
//import timber.log.Timber
//import java.text.DateFormat
//import java.util.*
//
//
//class LocationHelper(context: Context) : MutableLiveData<Location>() {
//    private val TAG = "LocationHelper"
//    private var instance: LocationHelper? = null
//    private var mContext: Context? = null
//    private val googleApiClient: GoogleApiClient? = null
//
//    // bunch of location related apis
//    private var mFusedLocationClient: FusedLocationProviderClient? = null
//    private var mSettingsClient: SettingsClient? = null
//    private var mLocationRequest: LocationRequest? = null
//    private var mLocationSettingsRequest: LocationSettingsRequest? = null
//    private var mLocationCallback: LocationCallback? = null
//    private var mCurrentLocation: Location? = null
//    private var mLastUpdateTime: String? = null
//
//    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10000
//    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 5000
//
//    fun LocationHelper(appContext: Context) {
//        mContext = appContext
//        buildGoogleApiClient(appContext)
//    }
//
//    private fun buildGoogleApiClient(appContext: Context) {
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext)
//        mSettingsClient = LocationServices.getSettingsClient(appContext)
//        mLocationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                super.onLocationResult(locationResult)
//                // location is received
//                Timber.d("onLocationResult: $locationResult")
//                mCurrentLocation = locationResult.lastLocation
//                mLastUpdateTime = DateFormat.getTimeInstance().format(Date())
//                updateLocationUI()
//            }
//        }
//        mLocationRequest = LocationRequest()
//        mLocationRequest?.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS)
//        mLocationRequest?.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
//        mLocationRequest?.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//        val builder = LocationSettingsRequest.Builder()
//        builder.addLocationRequest(mLocationRequest)
//        mLocationSettingsRequest = builder.build()
//        startLocationUpdates()
//    }
//
//
//    fun getInstance(appContext: Context?): LocationHelper? {
//        if (instance == null) {
//            instance = LocationHelper(appContext)
//        }
//        return instance
//    }
//
//    private fun updateLocationUI() {
//        if (mCurrentLocation != null) {
//            value = mCurrentLocation!!
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun startLocationUpdates() {
//        mSettingsClient
//            ?.checkLocationSettings(mLocationSettingsRequest)
//            ?.addOnSuccessListener {
//                Timber.d("All location settings are satisfied.")
//                Toast.makeText(mContext, "Started location updates!", Toast.LENGTH_SHORT).show()
//                mFusedLocationClient!!.requestLocationUpdates(
//                    mLocationRequest,
//                    mLocationCallback, Looper.myLooper()
//                )
//                updateLocationUI()
//            }
//            ?.addOnFailureListener { e ->
//                val statusCode = (e as ApiException).statusCode
//                when (statusCode) {
//                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
//                        Timber.d("Location settings are not satisfied. Attempting to upgrade location settings")
//                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
//                        Timber.d("Location settings are inadequate, and cannot be fixed here. Fix in Settings.")
//                    }
//                }
//                updateLocationUI()
//            }
//    }
//
//    fun stopLocationUpdates() {
//        // Removing location updates
//        mFusedLocationClient
//            ?.removeLocationUpdates(mLocationCallback)
//            ?.addOnCompleteListener {
//                Toast.makeText(
//                    mContext,
//                    "Location updates stopped!",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//    }
//}