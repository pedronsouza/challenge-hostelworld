package com.pedronsouza.feature.property_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.pedronsouza.shared.components.LocalDimensions
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun PropertyListScreen(
    snackbarHostState: SnackbarHostState,
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
            }
        }
    }

    LazyColumn(
        modifier = Modifier.padding(LocalDimensions.current.defaultScreenPadding)
    ) {
        items(state.value.properties) { item: PropertyListItem ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.sendEvent(PropertyListEvent.PropertySelected(item))
                    }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    AsyncImage(
                        model = item.images.first().toString(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(LocalDimensions.current.propertyShowroomImageSize)
                    )

                    Column(modifier = Modifier.padding(LocalDimensions.current.innerTextContentPropertyCardPadding)) {
                        Spacer(
                            modifier = Modifier
                                .height(LocalDimensions.current.defaultSpacingBetweenPropertyCards)
                        )

                        Text(
                            text = item.name,
                            fontWeight = FontWeight.Medium,
                            fontSize = LocalDimensions.current.propertyCardNameTextSize
                        )

                        Spacer(
                            modifier = Modifier
                                .height(LocalDimensions.current.defaultSpacingBetweenPropertyCards)
                        )

                        item.description?.let { description ->
                            Text(
                                text = description,
                                fontSize = LocalDimensions.current.propertyCardDescriptionTextSize
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier
                    .height(LocalDimensions.current.defaultSpacingBetweenPropertyCards)
            )
        }
    }
}