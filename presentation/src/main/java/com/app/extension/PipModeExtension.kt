package com.app.extension

import android.app.AppOpsManager
import android.app.PictureInPictureParams
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.os.Process
import android.util.Rational
import android.view.Display
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat


fun Context.checkPiPModePermission(): Boolean {
    try {
        val appOpsManager =
            this.getSystemService(AppCompatActivity.APP_OPS_SERVICE) as AppOpsManager
        if (appOpsManager != null) {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppOpsManager.MODE_ERRORED == appOpsManager?.unsafeCheckOpNoThrow(
                        AppOpsManager.OPSTR_PICTURE_IN_PICTURE,
                        Process.myUid(), packageName
                    )
                } else {
                    AppOpsManager.MODE_ERRORED == appOpsManager?.checkOpNoThrow(
                        AppOpsManager.OPSTR_PICTURE_IN_PICTURE,
                        Process.myUid(), packageName
                    )
                }
            } else false
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
    return false
}

fun pictureInPictureParamsBuilder(d: Display): PictureInPictureParams.Builder? {
    var mPictureInPictureParamsBuilder: PictureInPictureParams.Builder? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        mPictureInPictureParamsBuilder = PictureInPictureParams.Builder()
        // Hide the controls in picture-in-picture mode.
        val p = Point()
        d.getSize(p)
        val width = p.x
        val height = p.y
        // Calculate the aspect ratio of the PiP screen.
        val aspectRatio = Rational(width, height)
        mPictureInPictureParamsBuilder.setAspectRatio(aspectRatio)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mPictureInPictureParamsBuilder.setAutoEnterEnabled(true)
        }
    }
    return mPictureInPictureParamsBuilder
}


//fun adjustFullScreen(config: Configuration, window: Window) {
//    val decorView = window.decorView
//    if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                or View.SYSTEM_UI_FLAG_FULLSCREEN
//                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
//    } else {
//        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//    }
//}


fun adjustFullScreen(config: Configuration, window: Window) {
    val insetsController = ViewCompat.getWindowInsetsController(window.decorView)
    insetsController?.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        insetsController?.hide(WindowInsetsCompat.Type.systemBars())
    } else {
        insetsController?.show(WindowInsetsCompat.Type.systemBars())
    }
}