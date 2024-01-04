package com.dexciuq.android_services

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobWorkItem
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.AlarmManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.dexciuq.android_services.databinding.ActivityMainBinding
import timber.log.Timber
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = (service as? MyForegroundService.LocalBinder) ?: return
            val foregroundService = binder.getService()
            foregroundService.onProgressChanged = {
                binding.progressBar.progress = it
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Toast.makeText(this@MainActivity, "Service destroyed", Toast.LENGTH_SHORT).show()
            binding.progressBar.progress = 0
        }
    }
    private var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        setContentView(binding.root)

        binding.simpleService.setOnClickListener {
            val intent = MyService.newIntent(this, 25)
            startService(intent)
        }

        binding.foregroundService.setOnClickListener {
            val intent = MyForegroundService.newIntent(this)
            ContextCompat.startForegroundService(this, intent)

            // Example of stop service outside of service
            // val intent = MyForegroundService.newIntent(this)
            // stopService(intent)
        }

        binding.intentService.setOnClickListener {
            val intent = MyIntentService.newIntent(this)

            // Can be started as a foreground service or as a regular service
            // It is up to you
            ContextCompat.startForegroundService(this, intent)
        }

        binding.jobScheduler.setOnClickListener {
            val componentName = ComponentName(this, MyJobService::class.java)

            val jobInfo = JobInfo.Builder(MyJobService.JOB_ID, componentName)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .build()

            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent = MyJobService.newIntent(page++)
                jobScheduler.enqueue(jobInfo, JobWorkItem(intent))
            } else {
                val intent = MyIntentService2.newIntent(this, page++)
                startService(intent)
            }
        }

        binding.jobIntentService.setOnClickListener {
            MyJobIntentService.enqueue(this, page++)
        }

        binding.alarmManager.setOnClickListener {
            val alarmManager = getSystemService(AlarmManager::class.java)

            val calendar = Calendar.getInstance()
            calendar.add(Calendar.SECOND, 10)

            val intent = AlarmReceiver.newIntent(this)
            val pendingIntent = PendingIntent.getBroadcast(
                this, 100, intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            AlarmManagerCompat.setExact(
                alarmManager,
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }

        binding.workManager.setOnClickListener {
            val workManager = WorkManager.getInstance(applicationContext)
            workManager.enqueueUniqueWork(
                MyWorker.WORK_NAME,
                ExistingWorkPolicy.APPEND,
                MyWorker.makeRequest(page++)
            )
        }
    }

    override fun onStart() {
        super.onStart()
        bindService(MyForegroundService.newIntent(this), serviceConnection, 0)
    }

    override fun onStop() {
        unbindService(serviceConnection)
        super.onStop()
    }
}