package com.pedronsouza.feature.property_detail

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pedronsouza.shared.components.CardMode
import com.pedronsouza.shared.components.LocalDimensions
import com.pedronsouza.shared.components.PropertyMainInfoCard
import com.pedronsouza.shared.components.models.PropertyItem
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PropertyDetailScreen(
    propertyItem: PropertyItem,
    snackbarHostState: SnackbarHostState
) {
    val viewModel: PropertyDetailViewModel = koinViewModel(
        parameters = {
            parametersOf(propertyItem)
        }
    )

    val state = viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = false) {
        viewModel.sendEvent(PropertyDetailEvent.PreparePropertyData)
    }

    if (!state.value.isLoading) {
        state.value.propertyItem?.let { propertyItem ->
            Column(
                modifier = Modifier
                    .padding(LocalDimensions.current.defaultScreenPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                PropertyMainInfoCard(
                    item = propertyItem,
                    cardMode = CardMode.CAROUSEL
                )
            }
        }
    }

}