package com.example.antiquecollectorui.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.antiquecollectorui.MainActivity
import com.example.antiquecollectorui.R
import com.example.antiquecollectorui.data.repository.AntiqueRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class PeriodicReminderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: AntiqueRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Get total item count to personalize the message
            var itemCount = 0
            repository.getAllItems().collect { items ->
                itemCount = items.size
                return@collect
            }

            val message = when {
                itemCount == 0 -> "Start cataloguing your antiques — your collection is empty!"
                itemCount == 1 -> "You have 1 item in your collection. Time for a review?"
                else -> "You have $itemCount antiques in your collection. Time for a review?"
            }

            // Tap notification → open app to collection screen
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(context, NotificationChannels.CHANNEL_PERIODIC)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Time to review your collection!")
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                NotificationManagerCompat.from(context)
                    .notify(NotificationChannels.NOTIFICATION_ID_PERIODIC, notification)
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
