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
import com.app.data.datasource.db.AppDatabase
import com.app.data.datasource.db.dao.AppDao
import com.app.databinding.FragmentDashboardBinding
import com.app.domain.entity.LocationEntity
import com.app.extension.*
import com.app.interfaces.OnLocationOnListener
import com.app.services.locations.LocationUtil
import com.app.ui.base.BaseFragment
import com.app.ui.splash.SplashActivity
import com.app.utilities.APP_OVERLAY_REQUEST_CODE
import com.app.utilities.PERMISSION_REQUEST_CODE
import com.app.vm.LocationEvent
import com.app.vm.dashboard.DashboardVM
import com.app.vm.location.LocationVM
import com.app.vm.permission.PermissionVM
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class DashboardFragment : BaseFragment(AppLayout.fragment_dashboard), OnMapReadyCallback,
    GoogleMap.OnMapLoadedCallback {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private var mapFragment: SupportMapFragment? = null
    private var mMap: GoogleMap? = null
    private val permissionVM by viewModel<PermissionVM>()
    private val locationVM by viewModel<LocationVM>()
    private val dashboardVM by viewModel<DashboardVM>()

    //    private var isGPSEnabled = false
    private var appDao: AppDao? = null


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
//        locationVM.getLocationData.observe(this, {
//            setLocationResult(it)
//        })
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
    }


    private fun initialize() {
        binding.customSwitch.bringToFront()
        binding.customSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.tvName.resString = AppString.on_duty
                userDataManager.isDuty = true
                binding.tvName.snackBar(AppString.background_location_on)
                binding.tvName.textColor = AppColor.colorGreenLight
            } else {
                binding.tvName.resString = AppString.off_duty
                userDataManager.isDuty = false
                binding.tvName.snackBar(AppString.background_location_off)
                binding.tvName.textColor = AppColor.colorRedLight
            }
        }
        setData()
        appDao = AppDatabase.getInstance(requireActivity()).appDao()
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
        }
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
        Timber.d("Location-->${location.latitude},${location.longitude}")
//        animateCamera(mMap, location.latitude, location.longitude, 14F)

        locationVM.onEvent(LocationEvent.SaveLocation(location))

        GlobalScope.launch(Dispatchers.IO) {
            val allLocations = appDao?.getAllLocationDatas() //Read data
            if (allLocations != null) {
                Timber.d("Location final -->${allLocations.size}")
            }
        }

    }


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