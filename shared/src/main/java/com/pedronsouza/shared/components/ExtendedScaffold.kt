package com.pedronsouza.shared.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ExtendedScaffold(
    snackbarHostState: SnackbarHostState,
    screenTitle: MutableState<String>,
    navigationMode: MutableState<NavigationMode>,
    navHostController: NavHostController,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            AppToolbar(
                title = screenTitle,
                navigationMode = navigationMode,
                navHostController = navHostController
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = content
    )
}