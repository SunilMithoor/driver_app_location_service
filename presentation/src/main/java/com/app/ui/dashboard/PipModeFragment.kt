package com.app.ui.dashboard

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.databinding.FragmentPipModeBinding
import com.app.domain.extention.parseDate
import com.app.extension.*
import com.app.helpers.LocationUtil
import com.app.interfaces.OnLocationOnListener
import com.app.ui.base.BaseFragment
import com.app.utilities.CAMERA_ZOOM_4
import com.app.utilities.Locations
import com.app.vm.location.LocationVM
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.location
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class PipModeFragment : BaseFragment(AppLayout.fragment_pip_mode) {

    private var _binding: FragmentPipModeBinding? = null
    private val binding get() = _binding!!
    private val locationVM by viewModel<LocationVM>()
    private var mapView: MapView? = null
    private var mapboxMap: MapboxMap? = null
    private var prevLocation: Location? = null

    override fun onCreate(view: View) {
        activityCompat.hideSupportActionBar()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = _binding ?: FragmentPipModeBinding.inflate(inflater, container, false)
        mapView = binding.mapbox
        mapboxMap = binding.mapbox.getMapboxMap()

        mapboxMap?.loadStyleUri(
            Style.MAPBOX_STREETS
        ) {
            activity?.initLocationComponent(mapView)
//            context?.updateSettings(mapView)
            mapView?.location?.updateSettings {
                enabled = true
                pulsingEnabled = true
                checkLocationOn()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkLocationOn() {
        LocationUtil(requireActivity()).turnGPSOn(object : OnLocationOnListener {
            override fun locationStatus(isLocationOn: Boolean) {
                Timber.d("Location Status-->$isLocationOn")
                if (isLocationOn) {
                    observeLocationUpdates()
                } else {
                    Timber.d("Enable location provider")
                }
            }
        })
    }


    private fun observeLocationUpdates() {
        locationVM.getLocationData.observe(this, {
            setLocationResult(it)
        })
    }

    private fun setLocationResult(location: Location) {
        Timber.d(
            "Pip  Location-->${location.latitude},${location.longitude},${
                location.time.parseDate().toString()
            },${location.accuracy},${location.altitude}"
        )

        if (location != null) {
            if (isValidLocation(location)) {
                Timber.d("valid location")
                animateCamera(location)
                prevLocation = location
            } else {
                Timber.d("invalid location")
            }
        }
    }


    private fun animateCamera(location: Location) {
        animateCamera(mapboxMap, location, CAMERA_ZOOM_4)
        mapView?.gestures?.focalPoint = getPixelForCoordinate(mapboxMap, location)
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

}