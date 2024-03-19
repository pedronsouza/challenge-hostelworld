package com.pedronsouza.challenge

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pedronsouza.feature.property_detail.PropertyDetailScreen
import com.pedronsouza.feature.property_list.PropertyListScreen
import com.pedronsouza.feature.property_list.R
import com.pedronsouza.shared.AppScreen
import com.pedronsouza.shared.components.NavigationMode
import com.pedronsouza.shared.navigation.NavigationItem
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = NavigationItem.Home.route,
    snackbarHostState: SnackbarHostState,
    appBarTitle: MutableState<String>,
    navigationMode: MutableState<NavigationMode>
) {
    val navHostScope = rememberCoroutineScope()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(NavigationItem.Home.route) {
            navigationMode.value = NavigationMode.NONE
            appBarTitle.value = stringResource(id = R.string.property_list_screen_title)

            PropertyListScreen(
                onShowSnackBarMessage = { message ->
                    navHostScope.launch { snackbarHostState.showSnackbar(message) }
                },

                onNavigateTo = { route ->
                    navController.navigate(route)
                },

                appScope = navHostScope,
            )
        }

        composable(
            route = NavigationItem.Detail.route,
            arguments = listOf(
                navArgument(AppScreen.DETAIL.parameters?.get(0).orEmpty()) {
                    type = NavType.StringType
                },

                navArgument(AppScreen.DETAIL.parameters?.get(1).orEmpty()) {
                    type = NavType.StringType
                }

            )
        ) { stackEntry ->
            val propertyId = stackEntry.arguments?.getString(
                AppScreen.DETAIL.parameters?.get(0).orEmpty()
            )

            val propertyName = stackEntry.arguments?.getString(
                AppScreen.DETAIL.parameters?.get(1).orEmpty()
            )

            appBarTitle.value = propertyName.orEmpty()
            navigationMode.value = NavigationMode.BACK


            checkNotNull(propertyId)

            PropertyDetailScreen(
                propertyId = propertyId
            )
        }
    }
}