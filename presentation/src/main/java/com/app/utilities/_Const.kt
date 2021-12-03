package com.app.utilities

import com.app.utilities.Notifications.LOCATION_SERVICE
import com.app.utilities.Notifications.LOCATION_SERVICE_NOTIFICATION

interface NavigationDrawer {
    val resId: Int
    val stringRes: Int
}

data class Notification(override val resId: Int, override val stringRes: Int) : NavigationDrawer
data class Logout(override val resId: Int, override val stringRes: Int) : NavigationDrawer


//Google Direction Api call
const val URL_GOOGLE_API =
    "https://maps.googleapis.com/maps/api/geocode/json?sensor=false&key=%s&latlng=%s,%s"

//Map Zoom,Padding
const val CAMERA_ZOOM_1 = 14F
const val CAMERA_ZOOM_2 = 17F
const val CAMERA_ZOOM_3 = 12.0
const val CAMERA_ZOOM_4 = 16.0
const val CLUSTER_ZOOM_1 = 17F
const val CLUSTER_ZOOM_2 = 20F
const val MAP_PADDING = 100

//Keys
const val ACTIVE = "active"
const val INACTIVE = "inActive"

//Permission Code
const val PERMISSION_REQUEST_CODE = 101
const val APP_OVERLAY_REQUEST_CODE = 102
const val REQUEST_CHECK_SETTINGS = 103
const val REQUEST_ENABLE_GPS = 104
const val LOCATION_PERMISSION_REQUEST = 105

//Notifications
const val FOREGROUND_SERVICE_ID = 201
val CHANNEL_ID: String = LOCATION_SERVICE
val CHANNEL_NOTIFICATION_ID: Int = 1001
val CHANNEL_NAME: String = LOCATION_SERVICE_NOTIFICATION
val EXTRA_STARTED_FROM_NOTIFICATION = "started_from_notification"


object Notifications {
    const val LOCATION_SERVICE_NOTIFICATION = "LOCATION_SERVICE_NOTIFICATION"
    const val LOCATION_SERVICE = "LOCATION_SERVICE"
}

const val DEFAULT_LAT = 12.97194
const val DEFAULT_LNG = 77.59369

object Locations {
    var LOCATION_ACCURACY = 30
}




