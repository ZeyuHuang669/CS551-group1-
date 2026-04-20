package com.example.antiquecollectorui.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat

object NotificationChannels {

    const val CHANNEL_PERIODIC = "channel_periodic_reminder"
    const val CHANNEL_CONTEXT_AWARE = "channel_context_aware"

    const val NOTIFICATION_ID_PERIODIC = 1001
    const val NOTIFICATION_ID_INCOMPLETE = 2001

    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val periodicChannel = NotificationChannel(
                CHANNEL_PERIODIC,
                "Collection Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Periodic reminders to review your antique collection"
            }

            val contextChannel = NotificationChannel(
                CHANNEL_CONTEXT_AWARE,
                "Incomplete Item Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts when an item has missing information"
            }

            val manager = NotificationManagerCompat.from(context)
            manager.createNotificationChannel(periodicChannel)
            manager.createNotificationChannel(contextChannel)
        }
    }
}