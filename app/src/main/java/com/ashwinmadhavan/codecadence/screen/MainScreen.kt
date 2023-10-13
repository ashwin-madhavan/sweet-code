package com.github.af2905.jetpack_compose_navigation.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.ashwinmadhavan.codecadence.BottomNavigationItems
import com.ashwinmadhavan.codecadence.SetupNavigationHost
import com.ashwinmadhavan.codecadence.TopBarNavigation

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    scaffoldState: ScaffoldState,
    navController: NavHostController
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBarNavigation(navController = navController) },
        bottomBar = { BottomNavigationItems(navController = navController) }
    ) { padding ->
        SetupNavigationHost(
            navController = navController,
            modifier = Modifier.padding(padding)
        )
    }
}