package com.pedronsouza.shared.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ExtendedScaffold(
    snackbarHostState: SnackbarHostState,
    screenTitle: MutableState<String>,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = { AppToolbar(screenTitle) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = content
    )
}