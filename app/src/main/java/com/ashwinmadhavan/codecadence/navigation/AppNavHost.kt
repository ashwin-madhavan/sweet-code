package com.ashwinmadhavan.listmaker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ashwinmadhavan.codecadence.views.CategoryListScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.TaskListScreen.route
    ) {
        composable(Screens.TaskListScreen.route) {
            CategoryListScreen(navigate = { taskListName ->
                navController.navigate("${Screens.TaskDetailScreen.route}/$taskListName")
            })
        }
    }
}
