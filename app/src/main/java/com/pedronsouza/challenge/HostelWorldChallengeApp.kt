package com.pedronsouza.challenge

import android.app.Application
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.pedronsouza.data.DataModule
import com.pedronsouza.domain.DomainModule
import com.pedronsouza.feature.property_detail.FeaturePropertyDetailModule
import com.pedronsouza.feature.property_list.PropertyListFeatureModule
import com.pedronsouza.feature.property_list.PropertyListScreen
import com.pedronsouza.shared.SharedModule
import com.pedronsouza.shared.components.AppTheme
import com.pedronsouza.shared.components.ExtendedScaffold
import com.pedronsouza.shared.components.NavigationMode
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.compose.KoinContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

@Composable
fun HostelWorldChallengeApp(context: Application) {
    initDependencies(context)
    val snackbarHostState = remember { SnackbarHostState() }
    val navHostController = rememberNavController()
    val appBarTitle = remember { mutableStateOf("") }
    val navigationMode = remember { mutableStateOf(NavigationMode.NONE) }

    AppTheme {
        KoinContext {
            ExtendedScaffold(
                snackbarHostState = snackbarHostState,
                screenTitle = appBarTitle,
                navigationMode = navigationMode
            ) { _ ->
                AppNavHost(
                    navController = navHostController,
                    snackbarHostState = snackbarHostState,
                    appBarTitle = appBarTitle,
                    navigationMode = navigationMode
                )
            }
        }
    }
}

private fun initDependencies(context: Application) {
    val isDebugMode = true

    startKoin {
        androidLogger(level = if (isDebugMode) Level.DEBUG else Level.NONE)
        androidContext(context)

        modules(
            listOf(
                SharedModule,
                DataModule,
                DomainModule,
                PropertyListFeatureModule,
                FeaturePropertyDetailModule
            )
        )
    }
}