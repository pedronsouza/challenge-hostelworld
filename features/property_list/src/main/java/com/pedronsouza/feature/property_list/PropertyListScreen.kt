package com.pedronsouza.feature.property_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pedronsouza.domain.mappers.FakeProperty
import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.useCases.GetAvailableCurrenciesUseCase
import com.pedronsouza.domain.useCases.GetSelectedCurrencyUseCase
import com.pedronsouza.domain.useCases.LoadPropertiesUseCase
import com.pedronsouza.domain.useCases.SwitchSelectedCurrencyUseCase
import com.pedronsouza.domain.values.AppCurrency
import com.pedronsouza.domain.values.displayName
import com.pedronsouza.shared.AppScreen
import com.pedronsouza.shared.components.ErrorView
import com.pedronsouza.shared.components.LoadingView
import com.pedronsouza.shared.components.LocalColors
import com.pedronsouza.shared.components.LocalDimensions
import com.pedronsouza.shared.components.PropertyCard
import com.pedronsouza.shared.components.models.PropertyItem
import com.pedronsouza.shared.fakes.FakePropertyItem
import com.pedronsouza.shared.mappers.PropertyListMapper
import com.pedronsouza.shared.navigation.RouteFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication
import org.koin.dsl.module

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PropertyListScreen(
    onShowSnackBarMessage: (String) -> Unit,
    onNavigateTo: (String) -> Unit,
    appScope: CoroutineScope,
) {
    val viewModel: PropertyListViewModel = koinViewModel<PropertyListViewModel>()
    val state = viewModel.viewState.collectAsState()
    val context = LocalContext.current
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)


    LaunchedEffect(key1 = Unit) {
        if (state.value.isLoading) {
            viewModel.sendEvent(PropertyListEvent.LoadProperties)
        }

        viewModel.viewEffect.collectLatest { effect ->
            when (effect) {
                is PropertyListEffects.ShowErrorToast ->
                    onShowSnackBarMessage(context.getString(effect.textRef))
                is PropertyListEffects.NavigateTo ->
                    onNavigateTo(effect.finalRoute)
            }
        }
    }

    when {
        LocalInspectionMode.current -> PropertyListForPreview()
        state.value.isLoading -> LoadingView()

        !state.value.isLoading && state.value.error != null -> {
            val error = state.value.error
            checkNotNull(error)

            ErrorView(error) {
                viewModel.sendEvent(PropertyListEvent.LoadProperties)
            }
        }

        else -> {
            val selectedCurrency = state.value.selectedCurrency
            checkNotNull(selectedCurrency)
            assert(state.value.availableCurrencies.isNotEmpty())

            ModalBottomSheetLayout(
                sheetContent = {
                    SheetContent(
                        availableCurrencies = state.value.availableCurrencies,
                        selectedCurrency = selectedCurrency,
                        onCurrencySelected = { currency ->
                            appScope.launch {
                                modalBottomSheetState.hide()
                                viewModel.sendEvent(PropertyListEvent.SwitchCurrency(currency))
                            }
                        }
                    )
                },
                sheetState = modalBottomSheetState
            ) {
                PropertyList(
                    properties = state.value.properties,
                    onPropertySelected = {
                        viewModel.sendEvent(PropertyListEvent.PropertySelected(it))
                    },
                    selectedCurrency = selectedCurrency,
                    onSwitchCurrencyClicked = {
                        appScope.launch {
                            modalBottomSheetState.show()
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SheetContent(
    availableCurrencies: List<AppCurrency>,
    selectedCurrency: AppCurrency,
    onCurrencySelected: (AppCurrency) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(LocalColors.current.lightGray)
    ) {
        items(availableCurrencies) { currency ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onCurrencySelected.invoke(currency)
                    }
                    .padding(12.dp)
            ) {
                Text(text = currency.toString(), fontWeight = FontWeight.Bold)
                Text(text = currency.displayName, modifier = Modifier.padding(start = 6.dp))

                if (selectedCurrency == currency) {
                    Icon(
                        painter = rememberVectorPainter(image = Icons.Filled.Done),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
private fun PropertyListForPreview() {
    PropertyList(
        properties = mutableListOf<PropertyItem>().apply {
            for (i in 0..10) {
                plusAssign(FakePropertyItem)
            }
        },
        selectedCurrency = AppCurrency("EUR"),
        onPropertySelected = { },
        onSwitchCurrencyClicked = { }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PropertyList(
    properties: List<PropertyItem>,
    selectedCurrency: AppCurrency,
    onPropertySelected: (PropertyItem) -> Unit,
    onSwitchCurrencyClicked: () -> Unit
) {
    LazyColumn {
        stickyHeader {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LocalColors.current.lightGray)
            ) {

                Text(
                    text = "Prices Currency: ${selectedCurrency.displayName}",
                    modifier = Modifier
                        .padding(12.dp)
                        .weight(1.0f)
                )

                TextButton(
                    onClick = onSwitchCurrencyClicked
                ) {
                    Text(text = "Switch")
                }
            }
        }

        items(properties) { item: PropertyItem ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LocalDimensions.current.defaultScreenPadding)
                    .clickable {
                        onPropertySelected.invoke(item)
                    }
                    .testTag("property_list_card_${item.id}")
            ) {
                PropertyCard(item)
            }

            Spacer(
                modifier = Modifier
                    .height(LocalDimensions.current.defaultSpacingBetweenPropertyCards)
            )
        }
    }
}

@Composable
private fun PreviewKoinApplication(content: @Composable () -> Unit) {
    KoinApplication(
        moduleList = {
            listOf(
                module {
                    factory {
                        PropertyListViewModel(
                            loadPropertiesUseCase = object : LoadPropertiesUseCase {
                                override suspend fun execute(): Result<List<Property>> {
                                    return Result.success(listOf(FakeProperty))
                                }
                            },

                            getSelectedCurrencyUseCase = object : GetSelectedCurrencyUseCase {
                                override suspend fun execute(): Result<AppCurrency> {
                                    return Result.success(AppCurrency("EUR"))
                                }
                            },

                            getAvailableCurrenciesUseCase = object: GetAvailableCurrenciesUseCase {
                                override suspend fun execute(): Result<List<AppCurrency>> {
                                    return Result.success(emptyList())
                                }

                            },

                            switchSelectedCurrencyUseCase = object: SwitchSelectedCurrencyUseCase {
                                override fun execute(currency: AppCurrency): Result<Unit> {
                                    return Result.success(Unit)
                                }

                            },

                            propertyListMapper = object : PropertyListMapper {
                                override fun transform(inputData: List<Property>): List<PropertyItem> {
                                    return listOf(FakePropertyItem)
                                }
                            },

                            routeFactory = object : RouteFactory {
                                override fun createRoute(
                                    screen: AppScreen,
                                    parameters: List<String>?
                                ): String {
                                    return "fake_route"
                                }

                            }
                        ).apply {
                            sendEvent(PropertyListEvent.LoadProperties)
                        }
                    }
                }
            )
        },
        content = content
    )
}

@Preview
@Composable
fun previewPropertyListScreen() {
    PreviewKoinApplication {
        PropertyListScreen(
            onShowSnackBarMessage = { },
            onNavigateTo = { },
            appScope = CoroutineScope(Dispatchers.IO)
        )
    }
}
