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
import com.example.antiquecollectorui.domain.model.AntiqueItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class IncompleteItemWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: AntiqueRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val incompleteItems = mutableListOf<AntiqueItem>()

            repository.getAllItems().collect { items ->
                incompleteItems.addAll(
                    items.filter { item ->
                        item.estimatedValue == 0.0 || item.category.isBlank()
                    }
                )
                return@collect
            }

            if (incompleteItems.isEmpty()) return Result.success()

            // Notify for each incomplete item (up to 3 to avoid spam)
            incompleteItems.take(3).forEachIndexed { index, item ->
                val missingFields = buildList {
                    if (item.estimatedValue == 0.0) add("estimated value")
                    if (item.category.isBlank()) add("category")
                }.joinToString(" and ")

                val message = "\"${item.name}\" is missing $missingFields. Tap to complete it."

                // Tap → open detail/edit screen for this specific item
                val intent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    putExtra("navigateTo", "detail/${item.id}")
                }
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    item.id,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val notification = NotificationCompat.Builder(context, NotificationChannels.CHANNEL_CONTEXT_AWARE)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Incomplete item in your collection")
                    .setContentText(message)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    // Action button: tap to edit directly
                    .addAction(
                        R.drawable.ic_launcher_foreground,
                        "Complete Now",
                        pendingIntent
                    )
                    .build()

                if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                    NotificationManagerCompat.from(context)
                        .notify(NotificationChannels.NOTIFICATION_ID_INCOMPLETE + index, notification)
                }
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
