package com.pedronsouza.challenge

import android.os.Build
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pedronsouza.feature.property_detail.PropertyDetailScreen
import com.pedronsouza.feature.property_list.PropertyListItemParamType
import com.pedronsouza.feature.property_list.PropertyListScreen
import com.pedronsouza.feature.property_list.R
import com.pedronsouza.shared.AppScreen
import com.pedronsouza.shared.components.NavigationMode
import com.pedronsouza.shared.components.models.PropertyItem
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

                appScope = navHostScope
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
            navigationMode.value = NavigationMode.BACK

            val propertyItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                stackEntry.arguments?.getParcelable(
                    AppScreen.DETAIL.parameterName.orEmpty(),
                    PropertyItem::class.java
                )
            } else {
                stackEntry.arguments?.getParcelable(AppScreen.DETAIL.parameterName.orEmpty())
            }

            checkNotNull(propertyItem)

            appBarTitle.value = propertyItem.name

            PropertyDetailScreen(
                propertyItem = propertyItem
            )
        }
    }
}