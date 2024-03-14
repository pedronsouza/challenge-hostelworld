package com.pedronsouza.feature.property_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.pedronsouza.shared.components.LocalDimensions
import com.pedronsouza.shared.components.PropertyMainInfoCard
import com.pedronsouza.shared.components.models.PropertyItem
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun PropertyListScreen(
    snackbarHostState: SnackbarHostState,
    navController: NavController
) {
    val viewModel: PropertyListViewModel = koinViewModel<PropertyListViewModel>()
    val state = viewModel.viewState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.sendEvent(PropertyListEvent.LoadProperties)
        viewModel.viewEffect.collectLatest { effect ->
            when (effect) {
                is PropertyListEffects.ShowErrorToast -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(effect.textRef)
                    )
                }

                is PropertyListEffects.NavigateTo -> {
                    navController.navigate(effect.finalRoute)
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier.padding(LocalDimensions.current.defaultScreenPadding)
    ) {
        items(state.value.properties) { item: PropertyItem ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.sendEvent(PropertyListEvent.PropertySelected(item))
                    }
            ) {
                PropertyMainInfoCard(item)
            }

            Spacer(
                modifier = Modifier
                    .height(LocalDimensions.current.defaultSpacingBetweenPropertyCards)
            )
        }
    }
}