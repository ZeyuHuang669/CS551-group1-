package com.example.antiquecollectorui


import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AntiqueCollectorApplication : Application()

// Don't forget to register in AndroidManifest.xml:
// <application
//     android:name=".AntiqueCollectorApplication"
//     ...
