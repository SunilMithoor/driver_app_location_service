package com.app.utilities

interface NavigationDrawer {
    val resId: Int
    val stringRes: Int
}

data class Notification(override val resId: Int, override val stringRes: Int) : NavigationDrawer
data class Logout(override val resId: Int, override val stringRes: Int) : NavigationDrawer

const val URL_GOOGLE_API =
    "https://maps.googleapis.com/maps/api/geocode/json?sensor=false&key=%s&latlng=%s,%s"

const val ZOOM_CAMERA = 16
const val CLUSTER_ZOOM = 20F
//const val MARKER_CLUSTER_ZOOM = 12F
const val MARKER_CLUSTER_ZOOM = 17F
const val MARKER_DIMENSION = 60
const val ACTIVE = "active"
const val INACTIVE = "inActive"
const val MAP_DEFAULT_PADDING = 100

const val PERMISSION_REQUEST_CODE = 101


