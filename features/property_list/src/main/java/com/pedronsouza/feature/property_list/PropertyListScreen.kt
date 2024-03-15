package com.pedronsouza.feature.property_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.useCases.LoadPropertiesUseCase
import com.pedronsouza.shared.AppScreen
import com.pedronsouza.shared.components.LocalColors
import com.pedronsouza.shared.components.LocalDimensions
import com.pedronsouza.shared.components.NavigationMode
import com.pedronsouza.shared.components.PropertyCard
import com.pedronsouza.shared.components.brushes.shimmerBrush
import com.pedronsouza.shared.components.models.PropertyItem
import com.pedronsouza.shared.fakes.FakeProperty
import com.pedronsouza.shared.fakes.FakePropertyItem
import com.pedronsouza.shared.mappers.PropertyListMapper
import com.pedronsouza.shared.navigation.RouteFactory
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplication
import org.koin.dsl.module

@Composable
fun PropertyListScreen(
    snackbarHostState: SnackbarHostState,
    navController: NavController,
    appBarTitle: MutableState<String>,
    navigationMode: MutableState<NavigationMode>
) {
    val viewModel: PropertyListViewModel = koinViewModel<PropertyListViewModel>()
    val state = viewModel.viewState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val screenTitle = stringResource(id = R.string.property_list_screen_title)

    LaunchedEffect(key1 = true) {
        appBarTitle.value = screenTitle
        navigationMode.value = NavigationMode.NONE

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

        else ->
            PropertyList(
                properties = state.value.properties,
                onPropertySelected = {
                    viewModel.sendEvent(PropertyListEvent.PropertySelected(it))
                }
            )
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
        onPropertySelected = { }
    )
}

@Composable
fun PropertyList(
    properties: List<PropertyItem>,
    onPropertySelected: (PropertyItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(LocalDimensions.current.defaultScreenPadding)
    ) {
        items(properties) { item: PropertyItem ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
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
            navController = navHostController,
            appBarTitle = remember { mutableStateOf("") },
            navigationMode = remember { mutableStateOf(NavigationMode.NONE) }
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
