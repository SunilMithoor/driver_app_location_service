package com.app.services.locations

import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.app.domain.entity.db.LocationEntity
import com.app.domain.entity.request.FirebaseDatabaseRequest
import com.app.domain.extention.parseDate
import com.app.domain.interactor.FirebaseDatabaseInteractor
import com.app.domain.interactor.LocationServiceInteractor
import com.app.domain.manager.UserPrefDataManager
import com.app.extension.*
import com.app.helpers.LocationUtil
import com.app.interfaces.OnLocationOnListener
import com.app.notifications.NotificationHelper
import com.app.utilities.*
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import org.koin.android.ext.android.inject
import timber.log.Timber

class LocationService : LifecycleService() {


    private val mBinder: IBinder = LocalBinder()
    private val userDataManager by inject<UserPrefDataManager>()
    private val locationServiceInteractor by inject<LocationServiceInteractor>()
    private val firebaseDatabaseInteractor by inject<FirebaseDatabaseInteractor>()
    private var locationData: LiveData<Location>? = null
    private var prevLocation: Location? = null
    private var signalStrengthService: SignalStrengthService? = null
    private var signalStrength = 0
    private var batteryStrength = 0

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        buildNotification()
        return START_STICKY
    }

    //onCreate
    override fun onCreate() {
        super.onCreate()
        locationData = LocationLiveData(this)
        checkLocationOn()
        if (signalStrengthService == null) {
            signalStrengthService = SignalStrengthService(this)
        }
        signalStrengthService?.listenToSignalStrengthChanges()
        signalStrengthService?.listenSignalStrengths {
            signalStrength = it
        }
    }


    private fun buildNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(
                FOREGROUND_SERVICE_ID,
                NotificationHelper(this)
                    .getNotificationBuilder(
                        this, CHANNEL_ID, CHANNEL_NAME,
                        resString(AppString.app_service),
                        resString(AppString.location_service_running)
                    )
            )
        }
    }

    inner class LocalBinder : Binder() {
        // Return this instance of MyService so clients can call public methods
        val service: LocationService
            get() = this@LocationService
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        Timber.d("onBind called")
        return mBinder
    }

    override fun onRebind(intent: Intent) {
        Timber.d("onRebind called")
//        stopForeground(true)
        super.onRebind(intent)
    }


    override fun onUnbind(intent: Intent): Boolean {
        Timber.d("onUnbind called")
//        startForeground(CHANNEL_NOTIFICATION_ID, getNotification())
        return true // Ensures onRebind() is called when a client re-binds.
    }


    private fun checkLocationOn() {
        if (userDataManager.isDuty) {
            LocationUtil(this).checkGPSOn(object : OnLocationOnListener {
                override fun locationStatus(isLocationOn: Boolean) {
                    Timber.d("Location Status-->$isLocationOn")
                    if (isLocationOn) {
                        startLocationUpdates()
                    } else {
                        Timber.d("Enable location provider")
                    }
                }
            })
        }
    }


    private fun startLocationUpdates() {
        when {
            this.isLocationPermissionsGranted() -> {
                observeLocationUpdates()
            }
            else -> {
                Timber.d("Ask Permission")
            }
        }
    }

    private fun observeLocationUpdates() {
        if (userDataManager.isDuty) {
            locationData?.observe(this, {
                setLocationResult(it)
            })
        }
    }

    private fun setLocationResult(location: Location) {
        Timber.d(
            "Service Location-->${location.latitude},${location.longitude},${
                location.time.parseDate().toString()
            }"
        )

        if (location != null) {

            if (isValidLocation(location)) {
                batteryStrength = this.batteryLevel()
                lifecycleScope.launch {
                    locationServiceInteractor.insertLocationData(
                        LocationEntity(
                            null,
                            time = location.time.parseDate().toString(),
                            latitude = location.latitude,
                            longitude = location.longitude,
                            accuracy = location.accuracy,
                            altitude = location.altitude,
                            speed = location.speed,
                            bearing = location.bearing,
                            provider = location.provider,
                            signalStrength = signalStrength,
                            batteryStrength = batteryStrength,
                            distance = prevLocation?.distanceTo(location)
                        )
                    )
                }

                prevLocation = location
                sendDataToDb(
                    location,
                    signalStrength,
                    batteryStrength,
                    prevLocation?.distanceTo(location)
                )

            }
        }
    }

    private fun getAllLocationData() {
        lifecycleScope.launch {
            locationServiceInteractor.getAllLocationData()
        }
    }


    private fun sendDataToDb(
        location: Location, signalStrength: Int,
        batteryStrength: Int,
        distance: Float?
    ) {
        val jsonArray = JSONArray()
        val jsonObject = JSONObject()
        jsonObject.put("latitude", location.latitude)
        jsonObject.put("longitude", location.longitude)
        jsonObject.put("speed", location.speed)
        jsonObject.put("altitude", location.altitude)
        jsonObject.put("accuracy", location.accuracy)
        jsonObject.put("time", location.time.parseDate().toString())
        jsonObject.put("signalStrength", signalStrength)
        jsonObject.put("batteryStrength", batteryStrength)
        jsonObject.put("distance", distance)
        jsonArray.put(jsonObject)
        Timber.d("json location-->$jsonArray")
        lifecycleScope.launch {
            firebaseDatabaseInteractor.setDatabaseData(
                FirebaseDatabaseRequest(
                    jsonArray
                )
            )
        }
    }

    private fun isValidLocation(location: Location?): Boolean {
        return if (location?.accuracy!! <= Locations.LOCATION_ACCURACY) {
            if (prevLocation != null) {
                val locationAccuracy = prevLocation?.accuracy!! + location.accuracy
                locationAccuracy < prevLocation?.distanceTo(location)!!
            } else {
                true
            }
        } else {
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        signalStrengthService = null
    }
}