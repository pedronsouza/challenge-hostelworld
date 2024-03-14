package com.pedronsouza.feature.property_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pedronsouza.domain.models.Property
import com.pedronsouza.shared.components.ImageMode
import com.pedronsouza.shared.components.LocalDimensions
import com.pedronsouza.shared.components.PropertyMainInfoCard
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PropertyDetailScreen(
    property: Property,
    snackbarHostState: SnackbarHostState
) {
    val viewModel: PropertyDetailViewModel = koinViewModel(
        parameters = {
            parametersOf(property)
        }
    )

    val state = viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = false) {
        viewModel.sendEvent(PropertyDetailEvent.PreparePropertyData)
    }

    if (!state.value.isLoading) {
        state.value.propertyItem?.let { propertyItem ->
            Column(
                modifier = Modifier.padding(LocalDimensions.current.defaultScreenPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    PropertyMainInfoCard(
                        item = propertyItem,
                        imageMode = ImageMode.CAROUSEL
                    )
                }
            }
        }
    }

}