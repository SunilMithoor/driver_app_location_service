package com.app.ui.dashboard

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.app.data.datasource.remote.mqtt.MQTTCall
import com.app.databinding.FragmentDashboardBinding
import com.app.domain.entity.FirebaseCallResponse
import com.app.domain.entity.FirebaseDatabaseCallResponse
import com.app.domain.entity.MQTTCallResponse
import com.app.domain.entity.db.LocationEntity
import com.app.domain.entity.request.FirebaseDatabaseRequest
import com.app.domain.extention.parseDate
import com.app.domain.interactor.FirebaseDatabaseInteractor
import com.app.extension.*
import com.app.helpers.LocationUtil
import com.app.interfaces.OnLocationOnListener
import com.app.model.LocationState
import com.app.services.locations.LocationService
import com.app.ui.base.BaseFragment
import com.app.ui.base.hideLoader
import com.app.ui.splash.SplashActivity
import com.app.utilities.APP_OVERLAY_REQUEST_CODE
import com.app.utilities.CAMERA_ZOOM_4
import com.app.utilities.Locations
import com.app.utilities.PERMISSION_REQUEST_CODE
import com.app.vm.dashboard.DashboardVM
import com.app.vm.location.LocationVM
import com.app.vm.mqtt.MQTTVM
import com.app.vm.onboarding.OnBoardingVM
import com.app.vm.permission.PermissionVM
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.location
import com.markodevcic.peko.PermissionResult
import org.json.JSONArray
import org.json.JSONObject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class DashboardFragment : BaseFragment(AppLayout.fragment_dashboard) {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val permissionVM by viewModel<PermissionVM>()
    private val locationVM by viewModel<LocationVM>()
    private val dashboardVM by viewModel<DashboardVM>()
    private val onBoardingVM by viewModel<OnBoardingVM>()
    private val mqttVM by viewModel<MQTTVM>()
    private var mapView: MapView? = null
    private var mapboxMap: MapboxMap? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    //    private var isGPSEnabled = false
    private var prevLocation: Location? = null

    private val mqttCall by inject<MQTTCall>()


    override fun onCreate(view: View) {
        activityCompat.hideSupportActionBar()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = _binding ?: FragmentDashboardBinding.inflate(inflater, container, false)
        mapView = binding.mapbox
        mapboxMap = binding.mapbox.getMapboxMap()
        initMapbox()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onResume() {
        super.onResume()
        appOverlayPermission()
        val MQTT_USERNAME = "sunilmg"
        val MQTT_PWD = "Sunil@135"
//        mqttVM.connectMQTT(MQTT_USERNAME,MQTT_PWD)
    }


    private fun initialize() {
        binding.customSwitch.bringToFront()
        binding.customSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.tvName.resString = AppString.on_duty
                userDataManager.isDuty = true
//                binding.tvName.snackBar(AppString.background_location_on)
                binding.tvName.textColor = AppColor.colorGreenLight
                startServices()
                startWorker()
            } else {
                binding.tvName.resString = AppString.off_duty
                userDataManager.isDuty = false
//                binding.tvName.snackBar(AppString.background_location_off)
                binding.tvName.textColor = AppColor.colorRedLight
                stopServices()
                stopWorker()
            }
        }
        binding.fab.click {
            animateCamera(latitude, longitude)
        }
        setData()

//        mqttVM.getMQTTClientId()
//        mqttVM.isConnected()
        mqttCall.connects()
    }

    private fun initMapbox() {
        mapboxMap?.loadStyleUri(
            Style.MAPBOX_STREETS
        ) {
            if (mapboxMap?.isFullyLoaded() == true) {
                context?.initLocationComponent(mapView)
                mapView?.location?.updateSettings {
                    enabled = true
                    pulsingEnabled = true
                }
            }
        }
    }


    private fun checkLocationOn() {
        LocationUtil(requireActivity()).turnGPSOn(object : OnLocationOnListener {
            override fun locationStatus(isLocationOn: Boolean) {
                Timber.d("Location Status-->$isLocationOn")
                if (isLocationOn) {
//                        isGPSEnabled = isLocationOn
                    startLocationUpdates()
                    if (userDataManager.isDuty) {
                        startServices()
                    } else {
                        stopServices()
                    }
                } else {
                    Timber.d("Enable location provider")
                }
            }
        })
    }


    override fun observeLiveData() {
        permissionVM.permissionLiveData.observe(this, {
            setResult(it)
        })
        onBoardingVM.firebaseMessageTokenResponse.observe(viewLifecycleOwner) {
            fragmentActivity?.hideLoader()
            Timber.d("FirebaseTokenResponse-->${it}")
            when (it) {
                is FirebaseCallResponse.Success -> {
                    it.data.let { data ->
                        Timber.d("message token-->${data.token}")
                        userDataManager.fireBaseToken = data.token
                    }
                }
                is FirebaseCallResponse.Failure -> {
                    binding.constraintLayout.snackBar(it.throwable.message)
                }
                else -> {
                    binding.constraintLayout.snackBar(AppString.error_message)
                }
            }
        }

        onBoardingVM.firebaseDeviceIdResponse.observe(viewLifecycleOwner) {
            fragmentActivity?.hideLoader()
            Timber.d("FirebaseDeviceidResponse-->${it}")
            when (it) {
                is FirebaseCallResponse.Success -> {
                    it.data.let { data ->
                        Timber.d("device id-->${data.deviceId}")
                        userDataManager.deviceToken = data.deviceId
                    }
                }
                is FirebaseCallResponse.Failure -> {
                    binding.constraintLayout.snackBar(it.throwable.message)
                }
                else -> {
                    binding.constraintLayout.snackBar(AppString.error_message)
                }
            }
        }

        onBoardingVM.firebaseDatabaseResponse.observe(viewLifecycleOwner, {
            fragmentActivity?.hideLoader()
            Timber.d("FirebaseDatabaseResponse-->${it}")
            when (it) {
                is FirebaseDatabaseCallResponse.Success -> {
                    it.data.let { data ->
                        Timber.d("data sent-->${data.data}")
                    }
                }
                is FirebaseDatabaseCallResponse.Failure -> {
                    binding.constraintLayout.snackBar(it.throwable.message)
                }
                else -> {
                    binding.constraintLayout.snackBar(AppString.error_message)
                }
            }
        })


//        mqttVM.mqttGenerateClientIdResponse.observe(viewLifecycleOwner, {
//            fragmentActivity?.hideLoader()
//            Timber.d("MQTTCallResponse-->${it}")
//            when (it) {
//                is MQTTCallResponse.Success -> {
//                    it.data.let { data ->
//                        Timber.d("mqtt client id-->${data?.data}")
//                    }
//                }
//                is MQTTCallResponse.Failure -> {
//                    binding.constraintLayout.snackBar(it.throwable?.message)
//                }
//                else -> {
//                    binding.constraintLayout.snackBar(AppString.error_message)
//                }
//            }
//        })

        mqttVM.mqttConnectedResponse.observe(viewLifecycleOwner, {
            fragmentActivity?.hideLoader()
            Timber.d("MQTT Connected Response-->${it}")
            when (it) {
                is MQTTCallResponse.Success -> {
                    it.data.let { data ->
                        Timber.d("MQTT Connected Response-->${data?.data}")
                        if (data?.data == false) {
                            mqttVM.connectMQTT("", "")
                        } else {
//                            push data
                            mqttVM.publishMQTT("data is pushed")
                        }
                    }
                }
                is MQTTCallResponse.Failure -> {
                    binding.constraintLayout.snackBar(it.throwable?.message)
                }
                else -> {
                    binding.constraintLayout.snackBar(AppString.error_message)
                }
            }
        })


        mqttVM.mqttConnectResponse.observe(viewLifecycleOwner, {
            fragmentActivity?.hideLoader()
            Timber.d("MQTT Connect Response-->${it}")
            when (it) {
                is MQTTCallResponse.Success -> {
                    it.data.let { data ->
                        Timber.d("mqtt connected-->${data?.isConnected}")
                        Timber.d("mqtt client id-->${data?.clientId}")
                        Timber.d("mqtt serverURI-->${data?.serverURI}")
                        if (data?.isConnected == true) {
                            //send data
                        }

                    }
                }
                is MQTTCallResponse.Failure -> {
                    binding.constraintLayout.snackBar(it.throwable?.message)
                }
                else -> {
                    binding.constraintLayout.snackBar(AppString.error_message)
                }
            }
        })

        mqttVM.mqttSubscribeResponse.observe(viewLifecycleOwner, {
            fragmentActivity?.hideLoader()
            Timber.d("MQTT Subscribe Response-->${it}")
            when (it) {
                is MQTTCallResponse.Success -> {
                    it.data.let { data ->
                        Timber.d("mqtt connected-->${data?.isConnected}")
                        Timber.d("mqtt client id-->${data?.clientId}")
                        Timber.d("mqtt serverURI-->${data?.serverURI}")
                        if (data?.isConnected == true) {
                            //send data
                        }

                    }
                }
                is MQTTCallResponse.Failure -> {
                    binding.constraintLayout.snackBar(it.throwable?.message)
                }
                else -> {
                    binding.constraintLayout.snackBar(AppString.error_message)
                }
            }
        })

        mqttVM.mqttUnSubscribeResponse.observe(viewLifecycleOwner, {
            fragmentActivity?.hideLoader()
            Timber.d("MQTT UnSubscribe Response-->${it}")
            when (it) {
                is MQTTCallResponse.Success -> {
                    it.data.let { data ->
                        Timber.d("mqtt connected-->${data?.isConnected}")
                        Timber.d("mqtt client id-->${data?.clientId}")
                        Timber.d("mqtt serverURI-->${data?.serverURI}")
                        if (data?.isConnected == true) {
                            //send data
                        }

                    }
                }
                is MQTTCallResponse.Failure -> {
                    binding.constraintLayout.snackBar(it.throwable?.message)
                }
                else -> {
                    binding.constraintLayout.snackBar(AppString.error_message)
                }
            }
        })
        mqttVM.mqttPublishResponse.observe(viewLifecycleOwner, {
            fragmentActivity?.hideLoader()
            Timber.d("MQTT Publish Response-->${it}")
            when (it) {
                is MQTTCallResponse.Success -> {
                    it.data.let { data ->
                        Timber.d("mqtt connected-->${data?.isConnected}")
                        Timber.d("mqtt client id-->${data?.clientId}")
                        Timber.d("mqtt serverURI-->${data?.serverURI}")
                        if (data?.isConnected == true) {
                            //send data
                        }

                    }
                }
                is MQTTCallResponse.Failure -> {
                    binding.constraintLayout.snackBar(it.throwable?.message)
                }
                else -> {
                    binding.constraintLayout.snackBar(AppString.error_message)
                }
            }
        })

        mqttVM.mqttDisConnectResponse.observe(viewLifecycleOwner, {
            fragmentActivity?.hideLoader()
            Timber.d("MQTT Disconnect Response-->${it}")
            when (it) {
                is MQTTCallResponse.Success -> {
                    it.data.let { data ->
                        if (data?.data == true) {
                            Timber.d("MQTT Disconnect")
                        }

                    }
                }
                is MQTTCallResponse.Failure -> {
                    binding.constraintLayout.snackBar(it.throwable?.message)
                }
                else -> {
                    binding.constraintLayout.snackBar(AppString.error_message)
                }
            }
        })
    }

    private fun observeLocationUpdates() {
        locationVM.getLocationData.observe(this, {
            setLocationResult(it)
        })
    }

    private fun setData() {
        if (userDataManager.isDuty) {
            binding.customSwitch.isChecked = true
            binding.tvName.resString = AppString.on_duty
            binding.tvName.textColor = AppColor.colorGreenLight
        } else {
            binding.customSwitch.isChecked = false
            binding.tvName.resString = AppString.off_duty
            binding.tvName.snackBar(AppString.off_duty_msg)
            binding.tvName.textColor = AppColor.colorRedLight
            stopWorker()
        }
        onBoardingVM.getMessageToken()
        onBoardingVM.getDeviceId()

    }

    private fun startServices() {
        if (userDataManager.isUserLoggedIn && userDataManager.isDuty) {
            context?.startService(LocationService::class.java)
        }
    }

    private fun stopServices() {
        context?.stopService(LocationService::class.java)
    }

    private fun startWorker() {
        if (userDataManager.isUserLoggedIn && userDataManager.isDuty) {
            context?.startWorker()
        }
    }

    private fun stopWorker() {
        context?.stopWorker()
    }

    private fun appOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(requireActivity())) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + requireActivity().packageName)
                )
                startActivityForResult(intent, APP_OVERLAY_REQUEST_CODE)
            } else {
                checkBackgroundLocationPermission()
            }
        }
    }

    private fun checkBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                context?.alert(com.app.R.style.Dialog_Alert) {
                    setCancelable(false)
                    setTitle(AppString.permissions_required)
                    setMessage(AppString.background_location_msg_alert)
                    setPositiveButton(AppString.label_ok) { _, _ ->
                        permissionVM.checkPermissionsData(
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        )
                    }
                    setNegativeButton(AppString.label_cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                }
            } else {
                checkLocationOn()
            }
        } else {
            checkLocationOn()
        }
    }

    private fun setResult(r: PermissionResult) {
        when (r) {
            is PermissionResult.Granted -> {
                Timber.d("GRANTED-->")
                context?.toast(AppString.permissions_granted)
                context?.restartApplication(SplashActivity::class.java)
            }
            is PermissionResult.Denied.NeedsRationale -> {
                Timber.d("DENIED-->")
            }
            is PermissionResult.Denied.DeniedPermanently -> {
                Timber.d("DENIED PERM-->")
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    )
                ) {
                    // If User Checked 'Don't Show Again' checkbox for runtime permission, then navigate user to Settings
                    val i = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts(
                            "package",
                            requireActivity().packageName, null
                        )
                    )
                    startActivityForResult(i, PERMISSION_REQUEST_CODE)
                }
            }
            else -> {

            }
        }
    }


    private fun setLocationResult(location: Location) {
        Timber.d(
            "Dash Location-->${location.latitude},${location.longitude},${
                location.time.parseDate().toString()
            },${location.accuracy},${location.altitude}"
        )
//        mqttVM.publishMQTT("hello 1")
        if (location != null) {
//            sendDataToDb(location)
            latitude = location.latitude
            longitude = location.longitude
            if (isValidLocation(location)) {
                Timber.d("valid location")
                animateCamera(location)
                prevLocation = location
            } else {
                Timber.d("invalid location")
            }
        }
    }


    private fun sendDataToDb(location: Location) {
        val jsonArray = JSONArray()
        val jsonObject = JSONObject()
        jsonObject.put("latitude", location.latitude)
        jsonObject.put("longitude", location.longitude)
        jsonObject.put("speed", location.speed)
        jsonObject.put("altitude", location.altitude)
        jsonObject.put("accuracy", location.accuracy)
        jsonObject.put("time", location.time.parseDate().toString())
        jsonArray.put(jsonObject)
        Timber.d("json location-->$jsonArray")
        onBoardingVM.setDatabaseData(FirebaseDatabaseRequest(jsonArray))
    }

    private fun locationData() {
        val locationState: LocationState = locationVM.locationState.value
        if (locationState != null) {
            val datas: List<LocationEntity> = locationState.locations
            Timber.d("Location data size -->${datas.size}")
//            locationVM.onEvent(LocationEvent.DeleteLocationByCount(30))
//            getData()
        }
    }

//    private fun getData() {
//        val locationState: LocationState = locationVM.locationState.value
//        if (locationState != null) {
//            val datas: List<LocationEntity> = locationState.locations
//            Timber.d("Location data after delete size -->${datas.size}")
//        }
//    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Activity.RESULT_OK == resultCode) {
            when (requestCode) {
                PERMISSION_REQUEST_CODE -> {
                    context?.toast(AppString.permissions_granted)
                    context?.restartApplication(SplashActivity::class.java)
                }
                APP_OVERLAY_REQUEST_CODE -> {
                    context?.toast(AppString.permissions_granted)
                    context?.restartApplication(SplashActivity::class.java)
                }
            }
        }
    }

    /**
     * Initiate Location updated by checking Location/GPS settings is ON or OFF
     * Requesting permissions to read location.
     */
    private fun startLocationUpdates() {
        when {
//            !isGPSEnabled -> {
//                Timber.d("Enable Location")
//            }
            context?.isLocationPermissionsGranted() == true -> {
                observeLocationUpdates()
            }
            else -> {
                permissionVM.checkPermissionsData(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    }

//    private fun stopLocationUpdates()
//    {
//        locationVM.getLocationData.observe(this, {
//            setLocationResult(it)
//        })
//    }


    private fun animateCamera(location: Location) {
        animateCamera(mapboxMap, location, CAMERA_ZOOM_4)
        mapView?.gestures?.focalPoint = getPixelForCoordinate(mapboxMap, location)
    }

    private fun animateCamera(latitude: Double?, longitude: Double?) {
        if (latitude != null && longitude != null) {
            animateCamera(mapboxMap, latitude, longitude, CAMERA_ZOOM_4)
            mapView?.gestures?.focalPoint = getPixelForCoordinate(mapboxMap, latitude, longitude)
        }
    }


//    private fun isValidLocation(prevLocation: Location?,curLocation: Location?): Boolean {
//        return if (curLocation?.accuracy!! <= Locations.LOCATION_ACCURACY) {
//            if (prevLocation != null) {
//                val locationAccuracy = prevLocation?.accuracy + curLocation?.accuracy
//                locationAccuracy < prevLocation?.distanceTo(curLocation)
//            } else {
//                true
//            }
//        } else {
//            false
//        }
//    }


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

}