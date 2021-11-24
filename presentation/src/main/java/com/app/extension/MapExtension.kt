package com.app.extension

import android.content.Context
import com.app.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import timber.log.Timber


fun setDefaultMap(mMap: GoogleMap?) {
    try {
        if (mMap != null) {
            val boundsIndia = LatLngBounds(LatLng(23.63936, 68.14712), LatLng(28.20453, 97.34466))
            val padding = 0 // offset from edges of the map in pixels
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(boundsIndia, padding)
            mMap.animateCamera(cameraUpdate)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun zoomRoute(googleMap: GoogleMap?, lstLatLngRoute: List<LatLng?>?, padding: Int) {
    try {
        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return
        val boundsBuilder = LatLngBounds.Builder()
        for (latLngPoint in lstLatLngRoute) boundsBuilder.include(latLngPoint)
        val latLngBounds = boundsBuilder.build()
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, padding))
    } catch (e: Exception) {
    }
}


fun setGoogleMap(googleMap: GoogleMap?): GoogleMap? {
    googleMap?.mapType = GoogleMap.MAP_TYPE_NORMAL
    // Enable / Disable zooming controls
    googleMap?.uiSettings?.isMyLocationButtonEnabled = true
    googleMap?.uiSettings?.isMapToolbarEnabled = false
    googleMap?.uiSettings?.isCompassEnabled = true
    googleMap?.uiSettings?.isTiltGesturesEnabled = true
    googleMap?.uiSettings?.isRotateGesturesEnabled = true
    googleMap?.uiSettings?.isScrollGesturesEnabled = true
    googleMap?.uiSettings?.isZoomControlsEnabled = false
    googleMap?.uiSettings?.isZoomGesturesEnabled = true
    return googleMap
}

fun animateCamera(googleMap: GoogleMap?, latitude: Double?, longitude: Double?, zoomLevel: Float) {
    if (googleMap != null && latitude != null && longitude != null) {
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    latitude, longitude
                ), zoomLevel
            )
        )
    }
}

fun Context.setMapStyle(googleMap: GoogleMap?) {
    try {
        // Customise the styling of the base map using a JSON object defined
        // in a raw resource file.
        googleMap?.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(this, R.raw.uber_map_style)
        )
    } catch (e: Exception) {
        Timber.d("Can't find style. Error: $e ")
    }
}




