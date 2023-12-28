package com.dexciuq.android_services

import android.content.Context
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import timber.log.Timber

class MyWorker(
    context: Context,
    private val workerParams: WorkerParameters
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        // code will be in other thread (not main)
        Timber.i("doWork")
        val page = workerParams.inputData.getInt(PAGE, 0)
        for (i in 0 until 5) {
            Thread.sleep(1000)
            Timber.i("$page $i")
        }
        return Result.success()
    }

    companion object {
        private const val PAGE = "page"
        const val WORK_NAME = "work_name"

        fun makeRequest(page: Int): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<MyWorker>().apply {
                setInputData(workDataOf(PAGE to page))
                setConstraints(makeConstraints())
            }.build()
        }

        private fun makeConstraints(): Constraints {
            return Constraints.Builder()
                .setRequiresCharging(true)
                .build()
        }
    }
}