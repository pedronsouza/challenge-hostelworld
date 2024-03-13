package com.pedronsouza.shared

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable

@Composable
fun ExtendedScaffold(
    screenTitle: String,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = { AppToolbar(screenTitle) },
        content = content
    )
}