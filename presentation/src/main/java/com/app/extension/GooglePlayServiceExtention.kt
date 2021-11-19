package com.app.extension

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability


//fun Activity.checkPlayServicesAvailable() {
//    val apiAvailability = GoogleApiAvailability.getInstance()
//    val status = apiAvailability.isGooglePlayServicesAvailable(this)
//    if (status != ConnectionResult.SUCCESS) {
//        if (apiAvailability.isUserResolvableError(status)) {
//            apiAvailability.getErrorDialog(this, status, 1).show()
//        } else {
//            Timber.d("Google Play Services unavailable. This app will not work")
//        }
//    }
//}

fun Context.checkPlayServicesAvailable(): Boolean {
    return try {
        GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS
    } catch (e: Exception) {
        false
    }
}
