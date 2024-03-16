package com.pedronsouza.feature.property_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.useCases.GetAvailableCurrenciesUseCase
import com.pedronsouza.domain.useCases.GetSelectedCurrencyUseCase
import com.pedronsouza.domain.useCases.LoadPropertiesUseCase
import com.pedronsouza.domain.useCases.SaveSelectedCurrencyUseCase
import com.pedronsouza.domain.values.AppCurrency
import com.pedronsouza.domain.values.displayName
import com.pedronsouza.shared.AppScreen
import com.pedronsouza.shared.components.LocalColors
import com.pedronsouza.shared.components.LocalDimensions
import com.pedronsouza.shared.components.PropertyCard
import com.pedronsouza.shared.components.brushes.shimmerBrush
import com.pedronsouza.shared.components.models.PropertyItem
import com.pedronsouza.shared.fakes.FakeProperty
import com.pedronsouza.shared.fakes.FakePropertyItem
import com.pedronsouza.shared.mappers.PropertyListMapper
import com.pedronsouza.shared.navigation.RouteFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication
import org.koin.dsl.module

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PropertyListScreen(
    snackbarHostState: SnackbarHostState,
    navController: NavController
) {
    val viewModel: PropertyListViewModel = koinViewModel<PropertyListViewModel>()
    val state = viewModel.viewState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val modalBottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val screenScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        if (state.value.isLoading) {
            viewModel.sendEvent(PropertyListEvent.LoadProperties)
        }

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

    when {
        LocalInspectionMode.current -> PropertyListForPreview()
        state.value.isLoading -> LoadingView()

        !state.value.isLoading && state.value.error != null -> {
            val error = state.value.error
            checkNotNull(error)

            ErrorView(error, viewModel)
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
                            screenScope.launch {
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
                        screenScope.launch {
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
fun LoadingView() {
    val showShimmer = remember { mutableStateOf(true) }

    DefaultRootLazyColumn(userScrollEnabled = false) {
        items(10) {
            ConstraintLayout(
                modifier = Modifier.padding(LocalDimensions.current.defaultScreenPadding)
            ) {
                val (image, content, rating) = createRefs()

                val imageConstraint: ConstrainScope.() -> Unit = {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LocalDimensions.current.propertyShowroomImageSize)
                        .constrainAs(image, imageConstraint)
                        .background(
                            shimmerBrush(
                                targetValue = 1300f,
                                showShimmer = showShimmer.value
                            )
                        )
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(LocalDimensions.current.innerTextContentPropertyCardPadding)
                        .constrainAs(content) {
                            top.linkTo(image.bottom)
                            start.linkTo(image.start)
                        }
                        .background(
                            shimmerBrush(
                                targetValue = 1300f,
                                showShimmer = showShimmer.value
                            )
                        )
                )

                Box(
                    modifier = Modifier
                        .width(260.dp)
                        .height(64.dp)
                        .padding(
                            LocalDimensions.current.innerTextContentPropertyCardPadding
                        )
                        .constrainAs(rating) {
                            top.linkTo(content.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .background(
                            shimmerBrush(
                                targetValue = 1300f,
                                showShimmer = showShimmer.value
                            )
                        )
                )
            }
        }
    }
}

@Composable
internal fun ErrorView(error: Throwable, viewModel: PropertyListViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(LocalColors.current.white)
    ) {
        Icon(
            painter = rememberVectorPainter(image = Icons.Outlined.Warning),
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = LocalColors.current.mediumGray
        )


        Text(
            text = stringResource(
                id = R.string.something_went_wrong
            ) + error.message?.let { ": $it" }.orEmpty()
        )

        Button(
            onClick = {
                viewModel.sendEvent(PropertyListEvent.LoadProperties)
            }
        ) {
            Text(text = "Retry")
        }
    }
}

@Composable
private fun DefaultRootLazyColumn(
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit
) {
    LazyColumn(content = content, userScrollEnabled = userScrollEnabled)
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

                            saveSelectedCurrencyUseCase = object: SaveSelectedCurrencyUseCase {
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
                                    parameter: String?
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
    val snackbarHostState = remember { SnackbarHostState() }
    val navHostController = rememberNavController()

    PreviewKoinApplication {
        PropertyListScreen(
            snackbarHostState = snackbarHostState,
            navController = navHostController
        )
    }
}

@Preview
@Composable
fun previewLoadingView() {
    LoadingView()
}

@Preview
@Composable
fun previewErrorView() {
    PreviewKoinApplication {
        ErrorView(
            error = IllegalArgumentException("Error message here"),
            viewModel = koinViewModel()
        )
    }
}
