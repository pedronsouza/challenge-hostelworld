package com.pedronsouza.challenge

import android.app.Application
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.pedronsouza.data.DataModule
import com.pedronsouza.domain.DomainModule
import com.pedronsouza.feature.property_list.PropertyListFeatureModule
import com.pedronsouza.feature.property_list.PropertyListScreen
import com.pedronsouza.shared.AppTheme
import com.pedronsouza.shared.ExtendedScaffold
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

@Composable
fun HostelWorldChallengeApp(context: Application) {
    initDependencies(context)
    val snackbarHostState = remember { SnackbarHostState() }

    AppTheme {
        ExtendedScaffold(
            snackbarHostState = snackbarHostState,
            screenTitle = stringResource(id = R.string.app_name)
        ) {
            PropertyListScreen(snackbarHostState)
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
                DataModule,
                DomainModule,
                PropertyListFeatureModule
            )
        )
    }
}