package com.dexciuq.android_services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class MyForegroundService : Service() {

    private val scope = CoroutineScope(Dispatchers.Main)

    private val notificationBuilder by lazy { createNotificationBuilder() }

    private val notificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    var onProgressChanged: (Int) -> Unit = {}

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
        Timber.i("onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.i("onStartCommand")
        scope.launch {
            for (i in 0..100 step 5) {
                delay(1000)

                val notification = notificationBuilder
                    .setProgress(PERCENT_MAX_VALUE, i, false)
                    .build()

                notificationManager.notify(NOTIFICATION_ID, notification)
                onProgressChanged(i)

                Timber.i(i.toString())
            }
            // Stop service inside of service
            stopSelf()
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        Timber.i("onBind")
        return LocalBinder()
    }

    override fun onDestroy() {
        Timber.i("onDestroy")
        scope.cancel()
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createNotificationBuilder(): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Title")
            .setContentText("Text")
            .setSmallIcon(android.R.drawable.ic_menu_call)
            .setProgress(PERCENT_MAX_VALUE, 0, false)
            .setOnlyAlertOnce(true)
    }

    inner class LocalBinder : Binder() {
        fun getService(): MyForegroundService = this@MyForegroundService
    }

    companion object {

        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "Service Notification"

        private const val PERCENT_MAX_VALUE = 100

        fun newIntent(context: Context): Intent {
            return Intent(context, MyForegroundService::class.java)
        }
    }
}