package com.example.antiquecollectorui.ui.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antiquecollectorui.data.datastore.UserPreferences
import com.example.antiquecollectorui.data.datastore.UserPreferencesRepository
import com.example.antiquecollectorui.notifications.NotificationScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val notificationScheduler: NotificationScheduler
) : ViewModel() {

    val uiState: StateFlow<UserPreferences> =
        userPreferencesRepository.userPreferencesFlow
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserPreferences())

    fun onDarkModeToggled(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateDarkMode(enabled)
        }
    }

    fun onNotificationsToggled(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateNotificationsEnabled(enabled)
            // Immediately reschedule or cancel workers
            notificationScheduler.scheduleAll()
        }
    }

    fun onReminderIntervalChanged(days: Int) {
        viewModelScope.launch {
            userPreferencesRepository.updateReminderInterval(days)
            // Reschedule with new interval
            notificationScheduler.scheduleAll()
        }
    }
}
