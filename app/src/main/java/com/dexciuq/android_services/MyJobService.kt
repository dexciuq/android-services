package com.dexciuq.android_services

import android.app.job.JobParameters
import android.app.job.JobService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class MyJobService : JobService() {

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        Timber.i("onCreate")
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Timber.i("onStartJob")

        // Code will run in main thread, so we use coroutines to run in other thread
        scope.launch {
            for (i in 0 until 100) {
                delay(1000)
                Timber.i(i.toString())
            }

            // finish
            // if we call jobFinished then onStopJob won't call
            jobFinished(params, true)
        }

        // If code is sync we do false, otherwise if async then true
        return true
    }

    // This method will called when only OS kill our service
    override fun onStopJob(params: JobParameters?): Boolean {
        Timber.i("onStopJob")
        return true
    }

    override fun onDestroy() {
        Timber.i("onDestroy")
        scope.cancel()
        super.onDestroy()
    }

    companion object {
        const val JOB_ID = 111
    }
}