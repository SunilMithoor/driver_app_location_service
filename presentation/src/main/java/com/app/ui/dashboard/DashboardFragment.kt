package com.app.ui.dashboard

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
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
import com.app.extension.*
import com.app.ui.base.BaseFragment
import com.app.ui.sign_in.SignInActivity
import com.app.utilities.APP_OVERLAY_REQUEST_CODE
import com.app.utilities.PERMISSION_REQUEST_CODE
import com.app.vm.dashboard.DashboardVM
import com.app.vm.permission.PermissionVM
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.markodevcic.peko.PermissionResult
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class DashboardFragment : BaseFragment(AppLayout.fragment_dashboard), OnMapReadyCallback,
    GoogleMap.OnMapLoadedCallback {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val dashboardVM by viewModel<DashboardVM>()
    private var mapFragment: SupportMapFragment? = null
    private var mMap: GoogleMap? = null
    private val permissionVM by viewModel<PermissionVM>()

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
        appOverlayPermission()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun observeLiveData() {
        permissionVM.permissionLiveData.observe(this, {
            setResult(it)
        })
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
                    setTitle(getString(AppString.permissions_required))
                    setMessage(getString(AppString.background_location_msg_alert))
                    setPositiveButton(getString(AppString.label_ok)) { _, _ ->
                        permissionVM.checkPermissionsData(
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        )
                    }
                    setNegativeButton(getString(AppString.label_cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                }
            }
        }
    }

    private fun setResult(r: PermissionResult) {
        when (r) {
            is PermissionResult.Granted -> {
                Timber.d("GRANTED-->")
                context?.toast(AppString.permissions_granted)
                context?.restartApplication(SignInActivity::class.java)
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
                    context?.restartApplication(SignInActivity::class.java)
                }
                APP_OVERLAY_REQUEST_CODE -> {
                    context?.toast(AppString.permissions_granted)
                    context?.restartApplication(SignInActivity::class.java)
                }
            }
        }
    }

}