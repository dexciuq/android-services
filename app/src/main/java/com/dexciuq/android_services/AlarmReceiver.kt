package com.dexciuq.android_services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val notificationManager = ContextCompat.getSystemService(
                it,
                NotificationManager::class.java
            ) as NotificationManager

            createNotificationChannel(notificationManager)

            val notification = NotificationCompat.Builder(it, CHANNEL_ID)
                .setContentTitle("Title")
                .setContentText("Text")
                .setSmallIcon(android.R.drawable.ic_menu_call)
                .build()

            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {

        private const val NOTIFICATION_ID = 2
        private const val CHANNEL_ID = "alarm_channel_id"
        private const val CHANNEL_NAME = "Alarm Manager Notification"

        fun newIntent(context: Context): Intent {
            return Intent(context, AlarmReceiver::class.java)
        }
    }
}