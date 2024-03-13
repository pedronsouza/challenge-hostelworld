package com.pedronsouza.feature.property_list

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel

@Composable
fun PropertyListScreen() {
    val viewModel = koinViewModel<PropertyListViewModel>()
}