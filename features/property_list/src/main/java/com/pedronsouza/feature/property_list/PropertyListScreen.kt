package com.pedronsouza.feature.property_list

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.pedronsouza.shared.LocalDimensions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun PropertyListScreen(
    snackbarHostState: SnackbarHostState
) {
    val viewModel: PropertyListViewModel = koinViewModel<PropertyListViewModel>()
    val state = viewModel.viewState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.sendEvent(PropertyListEvent.LoadProperties)
    }

    LaunchedEffect(key1 = true) {
        viewModel.viewEffect.collectLatest { effect ->
            when (effect) {
                is PropertyListEffects.ShowErrorToast -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(effect.textRef)
                    )
                }
            }
        }
    }

    LazyColumn {
        items(state.value.properties) { item: PropertyListItem ->
            Card {
                AsyncImage(
                    model = item.images.first().toString(),
                    contentDescription = null,
                    modifier = Modifier.height(LocalDimensions.current.propertyShowroomImageSize)
                )
            }
        }
    }
}