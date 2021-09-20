package com.fyp.privacyguard.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.fyp.privacyguard.NOTIFICATION_CHANNEL_ID
import com.fyp.privacyguard.R
import com.fyp.privacyguard.core.MainActivity
import com.fyp.privacyguard.createNotificationChanel


@SuppressLint("UnspecifiedImmutableFlag")
fun Context.generateNotification(): Notification {

    val mainNotificationText = "Privacy Guard is ON"

    val titleText = getString(R.string.app_name)

    val bigTextStyle = NotificationCompat.BigTextStyle()
        .bigText(mainNotificationText)
        .setBigContentTitle(titleText)

    // 3. Set up main Intent/Pending Intents for notification.
    val launchActivityIntent = Intent(this, MainActivity::class.java)

    val activityPendingIntent = PendingIntent.getActivity(
        this, 0, launchActivityIntent, 0)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        createNotificationChanel()
    }

    // 4. Build and issue the notification.
    // Notification Channel Id is ignored for Android pre O (26).
    val notificationCompatBuilder =
        NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)

    return notificationCompatBuilder
        .setStyle(bigTextStyle)
        .setContentTitle(titleText)
        .setContentText(mainNotificationText)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setDefaults(NotificationCompat.DEFAULT_ALL)
        .setOngoing(true)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setContentIntent(activityPendingIntent)
        .build()
}
