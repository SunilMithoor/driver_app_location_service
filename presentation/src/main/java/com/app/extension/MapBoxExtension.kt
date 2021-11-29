package com.app.extension

import android.content.Context
import android.location.Location
import androidx.appcompat.content.res.AppCompatResources
import com.app.R
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.MapAnimationOptions.Companion.mapAnimationOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.gestures.getGesturesSettings
import com.mapbox.maps.plugin.locationcomponent.location

fun setMapBoxStyle(mapView: MapView?) {
    mapView?.getMapboxMap()?.loadStyleUri(Style.OUTDOORS)
}

fun getMapView(mapView: MapView): MapView? {
    return mapView
}


fun Context.initLocationComponent(mapView: MapView?) {
    val locationComponentPlugin = mapView?.location
    locationComponentPlugin?.updateSettings {
        this.enabled = true
        this.locationPuck = LocationPuck2D(
            bearingImage = AppCompatResources.getDrawable(
                this@initLocationComponent,
                R.drawable.mapbox_user_puck_icon,
            ),
            shadowImage = AppCompatResources.getDrawable(
                this@initLocationComponent,
                R.drawable.mapbox_user_icon_shadow,
            ),
            scaleExpression = interpolate {
                linear()
                zoom()
                stop {
                    literal(0.0)
                    literal(0.6)
                }
                stop {
                    literal(20.0)
                    literal(1.0)
                }
            }.toJson()
        )
    }
}

fun Context.updateSettings(mapView: MapView?) {
    mapView?.location?.updateSettings {
        enabled = true
        pulsingEnabled = true
    }
}


fun animateCamera(mapboxMap: MapboxMap?, location: Location, zoom: Double) {
    mapboxMap?.flyTo(
        cameraOptions {
            center(
                Point.fromLngLat(
                    location.longitude,
                    location.latitude
                )
            ) // Sets the new camera position on click point
            zoom(zoom) // Sets the zoom
            bearing(location.bearing.toDouble()) // Rotate the camera
            pitch(10.0) // Set the camera pitch
        },
        mapAnimationOptions {
            duration(1000)
        }
    )
}

fun animateCamera(mapboxMap: MapboxMap?, latitude: Double?, longitude: Double?, zoom: Double) {
    if (latitude != null && longitude != null) {
        mapboxMap?.flyTo(
            cameraOptions {
                center(
                    Point.fromLngLat(
                        longitude,
                        latitude
                    )
                ) // Sets the new camera position on click point
                zoom(zoom) // Sets the zoom
                pitch(10.0) // Set the camera pitch
            },
            mapAnimationOptions {
                duration(1000)
            }
        )
    }
}


fun getPixelForCoordinate(mapboxMap: MapboxMap?, location: Location): ScreenCoordinate? {
    return mapboxMap
        ?.pixelForCoordinate(Point.fromLngLat(location.longitude, location.latitude))
}

fun getPixelForCoordinate(
    mapboxMap: MapboxMap?,
    latitude: Double,
    longitude: Double
): ScreenCoordinate? {
    return mapboxMap
        ?.pixelForCoordinate(Point.fromLngLat(longitude, latitude))
}
