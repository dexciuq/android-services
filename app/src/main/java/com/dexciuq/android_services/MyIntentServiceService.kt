package com.dexciuq.android_services

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import timber.log.Timber

class MyIntentServiceService : IntentService(NAME) {

    override fun onCreate() {
        super.onCreate()

        // if value true then START_REDELIVER_INTENT else if value false START_NOT_STICKY
        // setIntentRedelivery(true)

        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        Timber.i("onCreate")
    }

    override fun onHandleIntent(intent: Intent?) {
        // Code inside of this method will be run in other thread (not main)
        // If you start service many times code will not be parallel (in usual services it is parallel)
        Timber.i("onHandleIntent")
        for (i in 0 until 5) {
            Thread.sleep(1000)
            Timber.i(i.toString())
        }
        // No need to stop service
    }

    override fun onDestroy() {
        Timber.i("onDestroy")
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createNotification(): Notification {
        createNotificationChannel()
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Title")
            .setContentText("Text")
            .setSmallIcon(android.R.drawable.ic_menu_call)
            .build()
    }

    companion object {
        private const val NAME = "intent_service"
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "Service Notification"

        fun newIntent(context: Context): Intent {
            return Intent(context, MyIntentServiceService::class.java)
        }
    }
}