package com.example.antiquecollectorui

import android.app.Application
import androidx.work.Configuration
import com.example.antiquecollectorui.notifications.NotificationChannels
import com.example.antiquecollectorui.notifications.NotificationScheduler
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class AntiqueCollectorApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerConfiguration: Configuration

    @Inject
    lateinit var notificationScheduler: NotificationScheduler

    override val workManagerConfiguration: Configuration
        get() = workerConfiguration

    override fun onCreate() {
        super.onCreate()

        // Create notification channels (must be done before any notification is shown)
        NotificationChannels.createChannels(this)

        // Schedule workers based on current preferences
        CoroutineScope(Dispatchers.IO).launch {
            notificationScheduler.scheduleAll()
        }
    }
}