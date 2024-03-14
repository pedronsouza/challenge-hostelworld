package com.pedronsouza.challenge

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pedronsouza.feature.property_detail.PropertyDetailScreen
import com.pedronsouza.shared.components.models.PropertyItem
import com.pedronsouza.feature.property_list.PropertyListItemParamType
import com.pedronsouza.feature.property_list.PropertyListScreen
import com.pedronsouza.shared.AppScreen
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
                snackbarHostState = snackbarHostState,
                navController
            )
        }

        composable(
            route = NavigationItem.Detail.route,
            arguments = listOf(
                    navArgument(AppScreen.DETAIL.parameterName.orEmpty()) {
                    type = PropertyListItemParamType()
                }
            )
        ) {stackEntry ->
            val propertyItem = stackEntry.arguments?.getParcelable(
                AppScreen.DETAIL.parameterName.orEmpty(),
                PropertyItem::class.java
            )

            checkNotNull(propertyItem)

            PropertyDetailScreen(
                propertyItem = propertyItem,
                snackbarHostState = snackbarHostState
            )
        }
    }
}