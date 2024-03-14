package com.pedronsouza.challenge

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pedronsouza.feature.property_list.PropertyListScreen
import com.pedronsouza.shared.navigation.NavigationItem

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = NavigationItem.Home.route,
    snackbarHostState: SnackbarHostState
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(NavigationItem.Home.route) {
            PropertyListScreen(
                snackbarHostState = snackbarHostState
            )
        }
    }
}