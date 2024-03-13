package com.pedronsouza.feature.property_list

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun PropertyListScreen() {
    val viewModel: PropertyListViewModel = koinViewModel<PropertyListViewModel>()
    val state = viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.sendEvent(PropertyListEvent.LoadProperties)
    }

    LaunchedEffect(key1 = true) {
        viewModel.viewEffect.collectLatest { effect ->
            when (effect) {
                is PropertyListEffects.ShowErrorToast -> TODO()
            }
        }
    }

    LazyColumn {
        items(state.value.properties) { item: PropertyListItem ->

        }
    }
}