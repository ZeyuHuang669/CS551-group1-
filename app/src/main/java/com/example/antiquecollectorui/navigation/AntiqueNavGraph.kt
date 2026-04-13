package com.example.antiquecollectorui.navigation

    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Modifier
    import androidx.navigation.NavHostController
    import androidx.navigation.NavType
    import androidx.navigation.compose.NavHost
    import androidx.navigation.compose.composable
    import androidx.navigation.navArgument
    import com.example.antiquecollectorui.ui.addedit.AddEditScreen
    import com.example.antiquecollectorui.ui.collection.CollectionScreen
    import com.example.antiquecollectorui.ui.detail.DetailScreen
    import com.example.antiquecollectorui.ui.history.HistoryScreen
    import com.example.antiquecollectorui.ui.preferences.PreferencesScreen

    @Composable
    fun AntiqueNavGraph(
        navController: NavHostController,
        modifier: Modifier = Modifier
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Collection.route,
            modifier = modifier
        ) {
            composable(Screen.Collection.route) {
                CollectionScreen(
                    onItemClick = { itemId ->
                        navController.navigate(Screen.Detail.createRoute(itemId))
                    },
                    onAddClick = {
                        navController.navigate(Screen.AddEdit.createRoute())
                    }
                )
            }

            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("itemId") { type = NavType.IntType })
            ) {
                DetailScreen(
                    onEditClick = { itemId ->
                        navController.navigate(Screen.AddEdit.createRoute(itemId))
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.AddEdit.route,
                arguments = listOf(
                    navArgument("itemId") {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ) {
                AddEditScreen(
                    onSaved = { navController.popBackStack() },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.History.route) {
                HistoryScreen(
                    onEventClick = { itemId ->
                        navController.navigate(Screen.Detail.createRoute(itemId))
                    }
                )
            }

            composable(Screen.Preferences.route) {
                PreferencesScreen()
            }
        }
    }
