package com.dexciuq.android_services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import timber.log.Timber

class MyIntentService2 : IntentService(NAME) {

    override fun onCreate() {
        super.onCreate()

        // if value true then START_REDELIVER_INTENT else if value false START_NOT_STICKY
        setIntentRedelivery(true)
        Timber.i("onCreate")
    }

    override fun onHandleIntent(intent: Intent?) {
        // Code inside of this method will be run in other thread (not main)
        // If you start service many times code will not be parallel (in usual services it is parallel)
        Timber.i("onHandleIntent")

        val page = intent?.extras?.getInt(EXTRA_PAGE) ?: 0
        for (i in 0 until 5) {
            Thread.sleep(1000)
            Timber.i("$page $i")
        }
        // No need to stop service
    }

    override fun onDestroy() {
        Timber.i("onDestroy")
        super.onDestroy()
    }

    companion object {
        private const val NAME = "intent_service_2"
        private const val EXTRA_PAGE = "page"

        fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, MyIntentService2::class.java).apply {
                putExtra(EXTRA_PAGE, page)
            }
        }
    }
}