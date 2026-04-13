package com.example.antiquecollectorui.data.datastore

    data class UserPreferences(
        val isDarkMode: Boolean = false,
        val notificationsEnabled: Boolean = true,
        val reminderIntervalDays: Int = 7  // default: remind every 7 days
    )