//package com.app.notifications
//
//import android.app.Notification
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.content.Context
//import android.content.ContextWrapper
//import android.graphics.Color
//import android.os.Build
//import androidx.core.app.NotificationCompat
//import com.app.R
//
//
///**
// * Helper class to manage notification channels, and create notifications.
// */
//class NotificationHelpers(ctx: Context?) : ContextWrapper(ctx) {
//    /**
//     * Get the notification manager.
//     *
//     * Utility method as this helper works with it a lot.
//     *
//     * @return The system service NotificationManager
//     */
//    private var manager: NotificationManager? = null
//        private get() {
//            if (field == null) {
//                field = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            }
//            return field
//        }
//
//    /**
//     * Get a notification of type 1
//     *
//     * Provide the builder rather than the notification it's self as useful for making notification
//     * changes.
//     *
//     * @param title the title of the notification
//     * @param body the body text for the notification
//     * @return the builder as it keeps a reference to the notification (since API 24)
//     */
//    fun getNotification1(title: String?, body: String?): Notification.Builder {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            Notification.Builder(applicationContext, PRIMARY_CHANNEL)
//                .setContentTitle(title)
//                .setContentText(body)
//                .setSmallIcon(smallIcon)
//                .setAutoCancel(true)
//        } else {
//            TODO("VERSION.SDK_INT < O")
//        }
//    }
//
//    /**
//     * Build notification for secondary channel.
//     *
//     * @param title Title for notification.
//     * @param body Message for notification.
//     * @return A Notification.Builder configured with the selected channel and details
//     */
//    fun getNotification2(title: String?, body: String?): NotificationCompat.Builder {
//        return NotificationCompat.Builder(applicationContext, SECONDARY_CHANNEL)
//            .setContentTitle(title)
//            .setContentText(body)
//            .setSmallIcon(smallIcon)
//            .setAutoCancel(true)
//    }
//
//    /**
//     * Send a notification.
//     *
//     * @param id The ID of the notification
//     * @param notification The notification object
//     */
//    fun notify(id: Int, notification: Notification.Builder) {
//        manager!!.notify(id, notification.build())
//    }
//
//    /**
//     * Get the small icon for this app
//     *
//     * @return The small icon resource id
//     */
//    private val smallIcon: Int
//        private get() = R.drawable.ic_launcher_notification
//
//    companion object {
//        const val PRIMARY_CHANNEL = "default"
//        const val SECONDARY_CHANNEL = "second"
//    }
//
//    /**
//     * Registers notification channels, which can be used later by individual notifications.
//     *
//     * @param ctx The application context
//     */
//    init {
//        val chan1 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel(
//                PRIMARY_CHANNEL,
//                getString(R.string.service_running), NotificationManager.IMPORTANCE_DEFAULT
//            )
//        } else {
//            TODO("VERSION.SDK_INT < O")
//        }
//        chan1.lightColor = Color.GREEN
//        chan1.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
//        manager!!.createNotificationChannel(chan1)
//        val chan2 = NotificationChannel(
//            SECONDARY_CHANNEL,
//            getString(R.string.service_running), NotificationManager.IMPORTANCE_HIGH
//        )
//        chan2.lightColor = Color.BLUE
//        chan2.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
//        manager!!.createNotificationChannel(chan2)
//    }
//}