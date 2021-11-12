package com.app.extension

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import timber.log.Timber


fun Context.isServiceRunning(serviceClass: Class<*>): Boolean {
    try {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
    } catch (e: SecurityException) {
        e.printStackTrace()
    }
    return false
}

@SuppressLint("BatteryLife")
fun Context.startService(serviceClass: Class<*>) {
    try {
        Timber.d("Service started -->$serviceClass")
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Timber.d("Permission denied")
        } else {
            if (!isServiceRunning(serviceClass)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ContextCompat.startForegroundService(this, Intent(this, serviceClass))
                } else {
                    startService(Intent(this, serviceClass))
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.stopService(serviceClass: Class<*>) {
    try {
        Timber.d("Service stopped-->$serviceClass")
        if (isServiceRunning(serviceClass)) {
            stopService(Intent(this, serviceClass))
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
}
