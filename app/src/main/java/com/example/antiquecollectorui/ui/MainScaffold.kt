package com.example.antiquecollectorui.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.antiquecollectorui.ui.screens.HomeScreen

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object Add : Screen("add", "Add Item")
    object Settings : Screen("settings", "Settings")
    object Detail : Screen("detail", "Detail")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AntiqueApp() {
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Antique Collector") },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            )
        },

        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == Screen.Home.route } == true,
                    onClick = { navController.navigate(Screen.Home.route) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )

                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == Screen.Add.route } == true,
                    onClick = { navController.navigate(Screen.Add.route) },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add Item") },
                    label = { Text("Add Item") }
                )

                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == Screen.Settings.route } == true,
                    onClick = { navController.navigate(Screen.Settings.route) },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onItemClick = {
                        navController.navigate(Screen.Detail.route)
                    }
                )
            }

            composable(Screen.Add.route) {
                Surface {
                    Text("Add Item Page")
                }
            }

            composable(Screen.Settings.route) {
                Surface {
                    Text("Settings Page")
                }
            }

            composable(Screen.Detail.route) {
                Surface {
                    Text("Detail Page")
                }
            }
        }
    }
}