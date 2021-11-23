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
import com.app.R
import com.app.databinding.FragmentDashboardBinding
import com.app.domain.entity.FirebaseCallResponse
import com.app.domain.entity.FirebaseDatabaseCallResponse
import com.app.domain.entity.db.LocationEntity
import com.app.domain.entity.request.FirebaseDatabaseRequest
import com.app.domain.extention.parseDate
import com.app.extension.*
import com.app.helpers.LocationUtil
import com.app.interfaces.OnLocationOnListener
import com.app.model.LocationState
import com.app.services.locations.LocationService
import com.app.ui.base.BaseFragment
import com.app.ui.base.hideLoader
import com.app.ui.splash.SplashActivity
import com.app.utilities.APP_OVERLAY_REQUEST_CODE
import com.app.utilities.PERMISSION_REQUEST_CODE
import com.app.vm.dashboard.DashboardVM
import com.app.vm.location.LocationVM
import com.app.vm.onboarding.OnBoardingVM
import com.app.vm.permission.PermissionVM
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.markodevcic.peko.PermissionResult
import org.json.JSONArray
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class DashboardFragment : BaseFragment(AppLayout.fragment_dashboard), OnMapReadyCallback,
    GoogleMap.OnMapLoadedCallback{

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private var mapFragment: SupportMapFragment? = null
    private var mMap: GoogleMap? = null
    private val permissionVM by viewModel<PermissionVM>()
    private val locationVM by viewModel<LocationVM>()
    private val dashboardVM by viewModel<DashboardVM>()
    private val onBoardingVM by viewModel<OnBoardingVM>()


    //    private var isGPSEnabled = false


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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMapFragment()
        initialize()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun observeLiveData() {
        permissionVM.permissionLiveData.observe(this, {
            setResult(it)
        })
        onBoardingVM.firebaseMessageTokenResponse.observe(viewLifecycleOwner, {
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
        })
        onBoardingVM.firebaseDeviceIdResponse.observe(viewLifecycleOwner, {
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
        })

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
    }

    private fun observeLocationUpdates() {
        if (userDataManager.isDuty) {
            locationVM.getLocationData.observe(this, {
                setLocationResult(it)
            })
        }
    }

    private fun checkLocationOn() {
        if (userDataManager.isDuty) {
            LocationUtil(requireActivity()).turnGPSOn(object : OnLocationOnListener {
                override fun locationStatus(isLocationOn: Boolean) {
                    Timber.d("Location Status-->$isLocationOn")
                    if (isLocationOn) {
//                        isGPSEnabled = isLocationOn
                        startLocationUpdates()
                    } else {
                        Timber.d("Enable location provider")
                    }
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        appOverlayPermission()
        locationData()
    }


    private fun initialize() {
        binding.customSwitch.bringToFront()
        binding.customSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.tvName.resString = AppString.on_duty
                userDataManager.isDuty = true
                binding.tvName.snackBar(AppString.background_location_on)
                binding.tvName.textColor = AppColor.colorGreenLight
                startService()
                startWorker()
            } else {
                binding.tvName.resString = AppString.off_duty
                userDataManager.isDuty = false
                binding.tvName.snackBar(AppString.background_location_off)
                binding.tvName.textColor = AppColor.colorRedLight
                stopService()
                stopWorker()
            }
        }
        setData()
    }


//     private fun call() {
//        lifecycleScope.launch {
//            flow {
//                delay(5000)
//                emit(true)
//            }.collect {
//                fragmentActivity?.showLoader()
////                onBoardingVM.getMessageToken()
//                onBoardingVM.getDeviceId()
//            }
//        }
//    }


    private fun setData() {
        if (userDataManager.isDuty) {
            binding.customSwitch.isChecked = true
            binding.tvName.resString = AppString.on_duty
            binding.tvName.textColor = AppColor.colorGreenLight
            startService()
            startWorker()
        } else {
            binding.customSwitch.isChecked = false
            binding.tvName.resString = AppString.off_duty
            binding.tvName.snackBar(AppString.off_duty_msg)
            binding.tvName.textColor = AppColor.colorRedLight
            stopService()
            stopWorker()
        }
        onBoardingVM.getMessageToken()
        onBoardingVM.getDeviceId()

    }

    private fun startService() {
        if (userDataManager.isDuty) {
            context?.startService(LocationService::class.java)
        }
    }

    private fun stopService() {
        context?.stopService(LocationService::class.java)
    }

    private fun startWorker() {
        if (userDataManager.isDuty) {
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
                context?.alert(R.style.Dialog_Alert) {
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
            }"
        )
//        animateCamera(mMap, location.latitude, location.longitude, 14F)

        if (location != null) {
//            sendDataToDb(location)
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


    private fun initMapFragment() {
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance()
            mapFragment!!.getMapAsync(this)
        }
        childFragmentManager.beginTransaction().replace(R.id.map, mapFragment!!).commit()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Timber.d("onMapReady called")
        mMap = googleMap
        fragmentActivity?.setMapStyle(mMap)
        setGoogleMap(mMap!!)
        mMap!!.setOnMapLoadedCallback(this)
    }

    override fun onMapLoaded() {
        Timber.d("onMapLoaded called")
        setDefaultMap(mMap)
    }

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


}