package com.pedronsouza.shared.components

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun AppToolbar(title: String) {
    TopAppBar(
        contentColor = LocalColors.current.toolbarTextColor,
        backgroundColor = LocalColors.current.lightGray,
        elevation = 0.dp,
        title = { Text(title) }
    )
}