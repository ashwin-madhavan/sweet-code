package com.ashwinmadhavan.codecadence

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ashwinmadhavan.codecadence.Routes.DETAIL_SCREEN
import com.ashwinmadhavan.codecadence.Routes.HOME_SCREEN
import com.ashwinmadhavan.codecadence.Routes.LOGS_SCREEN
import com.ashwinmadhavan.codecadence.ui.theme.PurpleGrey40
import com.ashwinmadhavan.codecadence.screen.LogsScreen.LogsScreen
import com.ashwinmadhavan.codecadence.screen.HomeScreen.CategoryDetailScreen
import com.ashwinmadhavan.codecadence.screen.HomeScreen.HomeScreen

data class Item(val route: String, val icon: ImageVector)

@Composable
fun BottomNavigationItems(navController: NavController) {
    val itemList = listOf(
        Item(route = HOME_SCREEN, icon = Icons.Filled.Home),
        Item(route = LOGS_SCREEN, icon = Icons.Filled.List)
    )

    BottomNavigation {
        val destinationChanged: MutableState<String?> = remember { mutableStateOf(null) }
        val isInParentRoute = itemList.firstOrNull { it.route == destinationChanged.value } != null
        val parentRoute: MutableState<String?> =
            remember(destinationChanged.value) { mutableStateOf(HOME_SCREEN) }

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        fun selectedBottomNavigationItem() = if (isInParentRoute) {
            parentRoute.value = currentRoute
            currentRoute
        } else {
            parentRoute.value
        }

        navController.addOnDestinationChangedListener { _, navDestination, _ ->
            destinationChanged.value = navDestination.route
        }

        itemList.forEach { item ->
            val isSelected = selectedBottomNavigationItem() == item.route
            BottomNavigationItem(
                icon = { Icon(imageVector = item.icon, contentDescription = null) },
                selectedContentColor = PurpleGrey40,
                unselectedContentColor = Color.Gray,
                alwaysShowLabel = false,
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun SetupNavigationHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = HOME_SCREEN
    ) {
        composable(route = HOME_SCREEN) {
            HomeScreen(onItemClick = { navController.navigate(DETAIL_SCREEN) })
        }
        composable(route = DETAIL_SCREEN) {
            CategoryDetailScreen()
        }
        composable(LOGS_SCREEN) {
            LogsScreen()
        }
    }
}

@Composable
fun TopBarNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavigationItemRouteList = listOf(HOME_SCREEN, LOGS_SCREEN)

    val title = when (currentRoute) {
        HOME_SCREEN -> stringResource(id = R.string.screen_home_title)
        LOGS_SCREEN -> stringResource(id = R.string.screen_log_title)
        DETAIL_SCREEN -> stringResource(id = R.string.screen_detail_title)
        else -> ""
    }

    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                color = Color.White
            )
        },
        backgroundColor = colorResource(id = R.color.colorPrimaryDark),
        navigationIcon =
        if (navController.previousBackStackEntry != null &&
            !bottomNavigationItemRouteList.contains(currentRoute)
        ) {
            {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        tint = Color.White,
                        contentDescription = null
                    )
                }
            }
        } else {
            null
        }
    )
}

object Routes {
    const val HOME_SCREEN: String = "home_screen"
    const val LOGS_SCREEN: String = "logs_screen"
    const val DETAIL_SCREEN: String = "detail_screen"
}