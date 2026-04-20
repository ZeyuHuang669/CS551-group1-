package com.example.antiquecollectorui.notifications

import android.content.Context
import androidx.work.*
import com.example.antiquecollectorui.data.datastore.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    companion object {
        const val WORK_PERIODIC_REMINDER = "work_periodic_reminder"
        const val WORK_INCOMPLETE_CHECK = "work_incomplete_check"
    }

    // Call this on app start and whenever preferences change
    suspend fun scheduleAll() {
        val prefs = userPreferencesRepository.userPreferencesFlow.first()

        if (prefs.notificationsEnabled) {
            schedulePeriodicReminder(prefs.reminderIntervalDays)
            scheduleIncompleteItemCheck(prefs.reminderIntervalDays)
        } else {
            cancelAll()
        }
    }

    private fun schedulePeriodicReminder(intervalDays: Int) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val request = PeriodicWorkRequestBuilder<PeriodicReminderWorker>(
            intervalDays.toLong(), TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .setInitialDelay(intervalDays.toLong(), TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_PERIODIC_REMINDER,
            ExistingPeriodicWorkPolicy.UPDATE, // reschedules if interval changed
            request
        )
    }

    private fun scheduleIncompleteItemCheck(intervalDays: Int) {
        // Check for incomplete items at half the reminder interval
        // e.g. if reminder is every 7 days, check incomplete every 3 days
        val checkIntervalDays = (intervalDays / 2).coerceAtLeast(1)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val request = PeriodicWorkRequestBuilder<IncompleteItemWorker>(
            checkIntervalDays.toLong(), TimeUnit.DAYS
        )
            .setConstraints(constraints)
            // First check after 1 day so newly added incomplete items get flagged quickly
            .setInitialDelay(1, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_INCOMPLETE_CHECK,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun cancelAll() {
        WorkManager.getInstance(context).apply {
            cancelUniqueWork(WORK_PERIODIC_REMINDER)
            cancelUniqueWork(WORK_INCOMPLETE_CHECK)
        }
    }
}
