package com.example.antiquecollectorui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.antiquecollectorui.data.datastore.UserPreferencesRepository
import com.example.antiquecollectorui.navigation.AntiqueNavGraph
import com.example.antiquecollectorui.navigation.Screen
import com.example.antiquecollectorui.ui.theme.AntiqueCollectorTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val initialPrefs = runBlocking { userPreferencesRepository.userPreferencesFlow.first() }
        setContent {
            val prefsFlow = userPreferencesRepository.userPreferencesFlow.collectAsState(initial = initialPrefs)
            val isDarkMode = prefsFlow.value.isDarkMode
            AntiqueCollectorTheme(darkTheme = isDarkMode) {
                AntiqueApp()
            }
        }
    }
}

@Composable
fun AntiqueApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { _ -> }  // permission result handled — user can also grant via settings

        LaunchedEffect(Unit) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val bottomNavItems = listOf(
        Triple(Screen.Collection.route, Icons.Default.CollectionsBookmark, "Collection"),
        Triple(Screen.History.route, Icons.Default.History, "History"),
        Triple(Screen.Preferences.route, Icons.Default.Settings, "Settings")
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { (route, icon, label) ->
                    NavigationBarItem(
                        selected = currentRoute == route,
                        onClick = {
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        AntiqueNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}