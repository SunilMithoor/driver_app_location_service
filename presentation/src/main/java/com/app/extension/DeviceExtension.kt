package com.app.extension

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.os.Build
import android.text.TextUtils

fun Context.restartApplication(myClass: Class<*>?) {
    try {
        val intent = Intent(this, myClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        Runtime.getRuntime().exit(0)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * Device name
 *
 * @return device name
 */
fun getDeviceNames(): String {
    try {
        return if (Build.MODEL.startsWith(Build.MANUFACTURER)) {
            capitalize(Build.MODEL)
        } else {
            capitalize(Build.MANUFACTURER) + " " + Build.MODEL
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return ""
}

/**
 * @param s
 * @return
 */
private fun capitalize(s: String?): String {
    try {
        if (s == null || s.isEmpty()) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first).toString() + s.substring(1)
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return ""
}

/**
 * Check app version name
 *
 * @return package version name
 */
fun Context.getVersionName(): String? {
    return try {
        packageManager.getPackageInfo(packageName, 0).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        ""
    }
}

fun Context.getVersionCode(): Int {
    return try {
        packageManager.getPackageInfo(packageName, 0).versionCode
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        0
    }
}

private fun Context.getAppVersion(): String {
    var result = ""
    try {
        result = packageManager
            .getPackageInfo(packageName, 0).versionName
        result = result.replace("[a-zA-Z]|-".toRegex(), "")
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return result
}


/**
 * To access the current device name
 */
fun getDeviceName(): String {
    try {
        val manufacturer = Build.MANUFACTURER
        return capitalizes(manufacturer)
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return ""
}

/**
 * To capitalize the device
 */
private fun capitalizes(str: String): String {
    if (TextUtils.isEmpty(str)) {
        return str
    }
    val arr = str.toCharArray()
    var capitalizeNext = true
    val phrase = StringBuilder()
    for (c in arr) {
        if (capitalizeNext && Character.isLetter(c)) {
            phrase.append(Character.toUpperCase(c))
            capitalizeNext = false
            continue
        } else if (Character.isWhitespace(c)) {
            capitalizeNext = true
        }
        phrase.append(c)
    }
    return phrase.toString()
}


/**
 * Check battery percentage
 *
 * @return battery percentage
 */
fun Context.batteryLevel(): Int {
    val intent = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    if (intent != null) {
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
        return level * 100 / scale
    }
    return 0
}