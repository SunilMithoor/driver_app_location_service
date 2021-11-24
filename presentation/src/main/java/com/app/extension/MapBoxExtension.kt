package com.app.extension

import android.content.Context
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.MapboxMap


fun Context.getToken() {
    Mapbox.getInstance(this, getString(AppString.access_token))
}

fun setMapBox(mapboxMap: MapboxMap?): MapboxMap? {
    mapboxMap?.uiSettings?.isAttributionEnabled = false
    mapboxMap?.uiSettings?.isLogoEnabled = true
    return mapboxMap
}