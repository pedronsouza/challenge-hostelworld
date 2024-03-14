package com.pedronsouza.shared.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable

@Composable
fun ExtendedScaffold(
    snackbarHostState: SnackbarHostState,
    screenTitle: String,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = { AppToolbar(screenTitle) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = content
    )
}