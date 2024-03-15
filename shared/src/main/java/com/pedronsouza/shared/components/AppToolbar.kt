package com.pedronsouza.shared.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun AppToolbar(
    title: MutableState<String>,
    navigationMode: MutableState<NavigationMode>
) {
    val navHostController = rememberNavController()
    val navigationIcon: @Composable (() -> Unit)? = when (navigationMode.value) {
        NavigationMode.NONE -> null
        NavigationMode.BACK -> BackIcon(navHostController)
    }

    TopAppBar(
        contentColor = LocalColors.current.toolbarTextColor,
        backgroundColor = LocalColors.current.lightGray,
        elevation = 0.dp,
        title = { Text(title.value) },
        navigationIcon = navigationIcon
    )
}

@Composable
private fun BackIcon(
    navHostController: NavHostController
): @Composable () -> Unit = {
    IconButton(onClick = {  }) {
        Icon(
            painter = rememberVectorPainter(image = Icons.AutoMirrored.Filled.ArrowBack),
            contentDescription = null
        )
    }
}

enum class NavigationMode {
    NONE,
    BACK
}

@Preview
@Composable
fun previewAppToolbarNoNavigation() {
    AppToolbar(
        title = remember {
            mutableStateOf("Home")
        },
        navigationMode =  remember {
            mutableStateOf(NavigationMode.NONE)
        }
    )
}

@Preview
@Composable
fun previewAppToolbarNavigation() {
    AppToolbar(
        title = remember {
            mutableStateOf("DETAILS")
        },
        navigationMode =  remember {
            mutableStateOf(NavigationMode.BACK)
        }
    )
}