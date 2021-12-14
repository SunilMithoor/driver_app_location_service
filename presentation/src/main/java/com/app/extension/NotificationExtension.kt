package com.app.extension

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.app.R
import com.app.services.locations.LocationService
import com.app.ui.splash.SplashActivity
import com.app.utilities.CHANNEL_ID
import com.app.utilities.CHANNEL_NAME
import com.app.utilities.CHANNEL_NOTIFICATION_ID
import com.app.utilities.EXTRA_STARTED_FROM_NOTIFICATION


private var notificationManager: NotificationManager? = null

fun Context.getManager(): NotificationManager? {
    if (notificationManager == null) {
        notificationManager =
            getSystemService(ContextWrapper.NOTIFICATION_SERVICE) as NotificationManager
    }
    return notificationManager
}

fun Context.createNotification() {

    // Android O requires a Notification Channel.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Create the channel for the notification
        val mChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        // Set the Notification Channel for the Notification Manager.
        getManager()?.createNotificationChannel(mChannel)
    }
}

fun Context.getNotification(): Notification {
    val intent = Intent(this, LocationService::class.java)
    // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
    intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true)
    // The PendingIntent that leads to a call to onStartCommand() in this service.
    val servicePendingIntent = PendingIntent.getService(
        this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
    )
    // The PendingIntent to launch activity.
    val activityPendingIntent = PendingIntent.getActivity(
        this, 0, Intent(this, SplashActivity::class.java), 0
    )

    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        .addAction(0, getString(R.string.notification_action_launch), activityPendingIntent)
        .addAction(0, getString(R.string.notification_action_stop), servicePendingIntent)
        .setContentTitle(getString(R.string.notification_content_title))
        .setContentText(getString(R.string.notification_content_text))
        .setOngoing(true)
        .setPriority(1) // Notification.PRIORITY_HIGH
        .setSmallIcon(R.drawable.ic_notifications)
        .setWhen(System.currentTimeMillis())

    // Set the Channel ID for Android O.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        builder.setChannelId(CHANNEL_ID) // Channel ID
    }
    return builder.build()
}


private fun getSmallIcon(): Int {
    return R.drawable.ic_notifications
}

fun Context.cancelNotification() {
    getManager()?.cancel(CHANNEL_NOTIFICATION_ID)
}

