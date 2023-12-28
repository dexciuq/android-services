package com.dexciuq.android_services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class MyService : Service() {

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        Timber.i("onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.i("onStartCommand")
        val start = intent?.extras?.getInt(EXTRA_START) ?: 0
        scope.launch {
            for (i in start until start + 100) {
                delay(1000)
                Timber.i(i.toString())
            }
        }
        // onStartCommand -> START_STICKY, START_NOT_STICKY, START_REDELIVER_INTENT
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? {
        Timber.i("onBind")
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        Timber.i("onDestroy")
        scope.cancel()
        super.onDestroy()
    }

    companion object {

        private const val EXTRA_START = "extra_start"
        fun newIntent(context: Context, start: Int): Intent {
            return Intent(context, MyService::class.java).apply {
                putExtra(EXTRA_START, start)
            }
        }
    }
}