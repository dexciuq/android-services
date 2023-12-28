package com.dexciuq.android_services

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dexciuq.android_services.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

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
            val intent = MyIntentServiceService.newIntent(this)

            // Can be started as a foreground service or as a regular service
            // It is up to you
            ContextCompat.startForegroundService(this, intent)
        }
    }
}