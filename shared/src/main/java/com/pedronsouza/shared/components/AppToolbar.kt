package com.pedronsouza.shared.components

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.dp

@Composable
fun AppToolbar(title: MutableState<String>) {
    TopAppBar(
        contentColor = LocalColors.current.toolbarTextColor,
        backgroundColor = LocalColors.current.lightGray,
        elevation = 1.dp,
        title = { Text(title.value) }
    )
}