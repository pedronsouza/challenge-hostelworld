package com.pedronsouza.feature.property_detail

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pedronsouza.domain.mappers.FakeProperty
import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.useCases.GetPropertyByIdUseCase
import com.pedronsouza.shared.components.CardMode
import com.pedronsouza.shared.components.ErrorView
import com.pedronsouza.shared.components.LoadingView
import com.pedronsouza.shared.components.LocalDimensions
import com.pedronsouza.shared.components.PropertyCard
import com.pedronsouza.shared.components.models.PropertyItem
import com.pedronsouza.shared.fakes.FakePropertyItem
import com.pedronsouza.shared.mappers.PropertyListMapper
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

@Composable
fun PropertyDetailScreen(
    propertyId: String
) {
    val viewModel: PropertyDetailViewModel = koinViewModel(
        parameters = {
            parametersOf(propertyId)
        }
    )

    val state = viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = false) {
        viewModel.sendEvent(PropertyDetailEvent.LoadProperty)
    }

    when {
        state.value.isLoading -> LoadingView()
        !state.value.isLoading && state.value.error != null ->
            ErrorView(error = state.value.error!!) {
                viewModel.sendEvent(PropertyDetailEvent.LoadProperty)
            }
        else -> {
            val propertyItem = state.value.propertyItem
            checkNotNull(propertyItem)

            Card(
                modifier = Modifier
                    .padding(LocalDimensions.current.defaultScreenPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                PropertyCard(
                    item = propertyItem,
                    cardMode = CardMode.CAROUSEL
                )
            }
        }
    }
}

@Preview
@Composable
fun previewPropertyDetailScreen() {
    KoinApplication(
        moduleList = {
            listOf(
                module {
                    factory {
                        PropertyDetailViewModel(
                            "test-id",
                            object: GetPropertyByIdUseCase {
                                override suspend fun execute(propertyId: String): Result<Property> =
                                    Result.success(FakeProperty)
                            },
                            object: PropertyListMapper {
                                override fun transform(inputData: List<Property>): List<PropertyItem> =
                                    listOf(FakePropertyItem)
                            }
                        )
                    }
                }
            )
        }
    ) {
        PropertyDetailScreen(
            propertyId = "test-id"
        )
    }
}