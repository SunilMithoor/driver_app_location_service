package com.app.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.app.R
import com.app.ui.dashboard.DashboardActivity

//class NotificationHelper constructor(base: Context?) : ContextWrapper(base) {
//
//    private var manager: NotificationManager? = null
//
//    fun createNotificationHelper(channelId: String?, channelName: String?) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val chanl = NotificationChannel(
//                channelId,
//                channelName,
//                NotificationManager.IMPORTANCE_LOW
//            )
//            chanl.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
//            chanl.description = "no sound"
//            chanl.setSound(null, null)
//            chanl.enableLights(false)
//            chanl.lightColor = Color.BLUE
//            chanl.enableVibration(false)
//            getManager()?.createNotificationChannel(chanl)
//        }
//    }
//
//    fun getLocationNotification(
//        context: Context,
//        CHANNEL_ID: String?
//    ): NotificationCompat.Builder {
//        val builder = NotificationCompat.Builder(
//            this,
//            CHANNEL_ID!!
//        )
//        builder.setSmallIcon(getSmallIcon())
//        builder.priority = NotificationCompat.PRIORITY_DEFAULT
//        builder.setShowWhen(true)
//        builder.setCategory(Notification.CATEGORY_SERVICE)
//        val intent = Intent(context, DashboardActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        builder.setContentTitle(getString(AppString.service_running))
//        builder.setTicker(getString(AppString.service_running))
//        builder.color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
//        builder.setContentIntent(PendingIntent.getActivity(this, 0, intent, 0))
//        return builder
//    }
//
//    private fun getSmallIcon(): Int {
//        return R.drawable.ic_launcher_notification
//    }
//
//    private fun getManager(): NotificationManager? {
//        if (manager == null) {
//            manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        }
//        return manager
//    }
//
//}


//class NotificationHelper constructor(context: Context) : ContextWrapper(context) {
//
//    private var notificationManager: NotificationManager? = null
//
//    companion object {
//        private const val MESSAGE_NOTIFICATION_TITLE = "Message Notification Channel"
//        private const val COMMENT_NOTIFICATION_TITLE = "Comment Notification Channel"
//        private const val DEFAULT_NOTIFICATION_TITLE = "Application Notification"
//        const val MESSAGE_NOTIFICATION_CHANNEL = "com.app.message_channel"
//        const val COMMENT_NOTIFICATION_CHANNEL = "com.app.comment_channel"
//        private const val DEFAULT_NOTIFICATION_CHANNEL = "com.app.default_channel"
//    }
//
//    init {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//            createChannels()
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private fun createChannels() {
//        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val notificationChannels = mutableListOf<NotificationChannel>()
//
//        // Building message notification channel
//
//        val messageNotificationChannel = NotificationChannel(
//            MESSAGE_NOTIFICATION_CHANNEL,
//            MESSAGE_NOTIFICATION_TITLE, NotificationManager.IMPORTANCE_HIGH
//        )
//         messageNotificationChannel.lightColor = Color.RED
//        messageNotificationChannel.setShowBadge(true)
//        messageNotificationChannel.setSound(uri, null)
//        messageNotificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
//        notificationChannels.add(messageNotificationChannel)
//
//        // Building comment notification channel
//
//        val commentNotificationChannel = NotificationChannel(
//            COMMENT_NOTIFICATION_CHANNEL,
//            COMMENT_NOTIFICATION_TITLE, NotificationManager.IMPORTANCE_HIGH
//        )
//        commentNotificationChannel.enableLights(true)
//        commentNotificationChannel.lightColor = Color.RED
//        commentNotificationChannel.setShowBadge(true)
//        commentNotificationChannel.setSound(uri, null)
//        commentNotificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
//        notificationChannels.add(commentNotificationChannel)
//
//        // Building default notification channel
//
//        val defaultNotificationChannel = NotificationChannel(
//            DEFAULT_NOTIFICATION_CHANNEL,
//            DEFAULT_NOTIFICATION_TITLE, NotificationManager.IMPORTANCE_LOW
//        )
//        defaultNotificationChannel.setShowBadge(true)
//        defaultNotificationChannel.setSound(uri, null)
//        notificationChannels.add(defaultNotificationChannel)
//
//        notificationManager?.createNotificationChannels(notificationChannels)
//    }
//
//    fun createNotificationBuilder(title: String, body: String, cancelAble: Boolean = true, pendingIntent: PendingIntent? = null,channelId: String = DEFAULT_NOTIFICATION_CHANNEL
//    ): Notification.Builder {
//        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//            Notification.Builder(applicationContext, channelId)
//        else
//            Notification.Builder(applicationContext)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder.setSmallIcon(getSmallIcon())
//            builder.setColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
//        } else
//            builder.setSmallIcon(getSmallIcon())
//        if (pendingIntent != null)
//            builder.setContentIntent(pendingIntent)
//        builder.setContentTitle(title)
//            .setContentText(body)
//            .setStyle(Notification.BigTextStyle().bigText(body))
//            .setAutoCancel(cancelAble)
//        return builder
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    fun deleteChannel(channelId: String) = apply {
//        getManager()?.deleteNotificationChannel(channelId)
//    }
//
//    fun  makeNotification(builder: Notification.Builder, notificationId: Int) = apply {
//        getManager()?.notify(notificationId, builder.build())
//    }
//
//    fun cancelNotification(notificationId: Int) = apply {
//        getManager()?.cancel(notificationId)
//    }
//
//    private fun getSmallIcon(): Int {
//        return R.drawable.ic_launcher_notification
//    }
//
//    private fun getManager(): NotificationManager? {
//        if (notificationManager == null) {
//            notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        }
//        return notificationManager
//    }
//}


class NotificationHelper constructor(base: Context?) : ContextWrapper(base) {

    private var notificationManager: NotificationManager? = null
    private var notificationBuilder: NotificationCompat.Builder? = null


    private fun getManager(): NotificationManager? {
        if (notificationManager == null) {
            notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        }
        return notificationManager
    }

    fun getNotificationBuilder(
        context: Context,
        channelId: String,
        channelName: String,
        name: String?,
        title: String?
    ): Notification? {
        if (notificationBuilder == null) {
            notificationBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(getSmallIcon())
                .setContentTitle(name)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentText(title)
                .setContentIntent(getPendingIntent(context))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationBuilder?.setChannelId(channelId) // Channel ID
                getManager()?.createNotificationChannel(
                    getNotificationChannel(
                        channelId,
                        channelName
                    )
                )
            }
            return notificationBuilder?.build()
        }
        return notificationBuilder?.build()
    }

    private fun getSmallIcon(): Int {
        return R.drawable.ic_launcher_notification
    }

    private fun getPendingIntent(context: Context): PendingIntent? {
        val notificationIntent = Intent(context, DashboardActivity::class.java)
        return PendingIntent.getActivity(
            context, 0,
            notificationIntent, 0
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNotificationChannel(
        channelId: String?,
        channelName: String?
    ): NotificationChannel {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_LOW
        )
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        channel.description = "no sound"
        channel.setSound(null, null)
        channel.enableLights(false)
        channel.lightColor = Color.BLUE
        channel.enableVibration(false)
        return channel
    }

}