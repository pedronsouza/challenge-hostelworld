package com.pedronsouza.feature.property_detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.pedronsouza.domain.models.Property
import com.pedronsouza.shared.components.LocalDimensions
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalFoundationApi::class)
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
                    val pagerState = rememberPagerState(
                        pageCount = { propertyItem.images.size }
                    )

                    HorizontalPager(state = pagerState) {
                        AsyncImage(
                            model = property.images.first().toString(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(LocalDimensions.current.propertyShowroomImageSize)
                        )
                    }


                    Column(
                        modifier = Modifier.padding(
                            LocalDimensions.current.innerTextContentPropertyCardPadding
                        )
                    ) {
                        Spacer(
                            modifier = Modifier
                                .height(LocalDimensions.current.defaultSpacingBetweenPropertyCards)
                        )

                        Text(
                            text = property.name,
                            fontWeight = FontWeight.Medium,
                            fontSize = LocalDimensions.current.propertyCardNameTextSize
                        )

                        Spacer(
                            modifier = Modifier
                                .height(LocalDimensions.current.defaultSpacingBetweenPropertyCards)
                        )

                        property.description.let { description ->

                        }
                    }
                }
            }
        }
    }

}