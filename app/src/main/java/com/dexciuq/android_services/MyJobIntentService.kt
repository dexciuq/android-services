package com.dexciuq.android_services

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import timber.log.Timber

class MyJobIntentService : JobIntentService() {

    override fun onCreate() {
        super.onCreate()
        Timber.i("onCreate")
    }

    override fun onHandleWork(intent: Intent) {
        Timber.i("onHandleWork")

        val page = intent.extras?.getInt(EXTRA_PAGE) ?: 0
        for (i in 0 until 5) {
            Thread.sleep(1000)
            Timber.i("$page $i")
        }
    }

    override fun onDestroy() {
        Timber.i("onDestroy")
        super.onDestroy()
    }

    companion object {

        private const val EXTRA_PAGE = "page"
        private const val JOB_ID = 111

        fun enqueue(context: Context, page: Int) {
            enqueueWork(
                context,
                MyJobIntentService::class.java,
                JOB_ID,
                newIntent(context, page)
            )
        }

        private fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, MyJobIntentService::class.java).apply {
                putExtra(EXTRA_PAGE, page)
            }
        }
    }
}