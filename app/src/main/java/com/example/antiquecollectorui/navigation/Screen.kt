package com.example.antiquecollectorui.navigation

    sealed class Screen(val route: String) {
        object Collection : Screen("collection")
        object Detail : Screen("detail/{itemId}") {
            fun createRoute(itemId: Int) = "detail/$itemId"
        }
        object AddEdit : Screen("addedit?itemId={itemId}") {
            fun createRoute(itemId: Int? = null) =
                if (itemId != null) "addedit?itemId=$itemId" else "addedit"
        }
        object History : Screen("history")
        object Preferences : Screen("preferences")
    }