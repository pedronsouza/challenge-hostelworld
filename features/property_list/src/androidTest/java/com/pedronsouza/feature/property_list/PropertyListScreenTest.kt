package com.pedronsouza.feature.property_list

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.pedronsouza.domain.mappers.FakeProperty
import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.useCases.GetAvailableCurrenciesUseCase
import com.pedronsouza.domain.useCases.GetSelectedCurrencyUseCase
import com.pedronsouza.domain.useCases.LoadPropertiesUseCase
import com.pedronsouza.domain.useCases.SaveSelectedCurrencyUseCase
import com.pedronsouza.domain.values.AppCurrency
import com.pedronsouza.shared.AppScreen
import com.pedronsouza.shared.components.models.PropertyItem
import com.pedronsouza.shared.fakes.FakePropertyItem
import com.pedronsouza.shared.mappers.PropertyListMapper
import com.pedronsouza.shared.navigation.RouteFactory
import com.pedronsouza.shared_test.CoilRule
import com.pedronsouza.shared_test.MainDispatcherRule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class PropertyListScreenTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val composeTestRule = createComposeRule(testDispatcher)

    @get:Rule
    val mainCoroutineRule = MainDispatcherRule(testDispatcher)

    @get:Rule
    val coilRule = CoilRule()

    private val loadPropertiesUseCase = object : LoadPropertiesUseCase {
        override suspend fun execute(): Result<List<Property>> =
            Result.success(listOf(FakeProperty))
    }

    private val getSelectedCurrencyUseCase = object: GetSelectedCurrencyUseCase {
        override suspend fun execute(): Result<AppCurrency> =
            Result.success(AppCurrency("EUR"))
    }

    private val getAvailableCurrenciesUseCase = object: GetAvailableCurrenciesUseCase {
        override suspend fun execute(): Result<List<AppCurrency>> =
            Result.success(listOf(AppCurrency("EUR")))

    }

    private val saveSelectedCurrencyUseCase = object: SaveSelectedCurrencyUseCase {
        override fun execute(currency: AppCurrency): Result<Unit> =
            Result.success(Unit)
    }

    private val propertyListMapper = object: PropertyListMapper {
        override fun transform(inputData: List<Property>): List<PropertyItem> =
            listOf(FakePropertyItem)
    }

    private val routeFactory = object: RouteFactory {
        override fun createRoute(screen: AppScreen, parameter: String?) = "/test_route"
    }

    private fun loadViewModel(viewModel: PropertyListViewModel) =
        startKoin {
            modules(module { factory { viewModel } })
        }.run {
            viewModel
        }


    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun testPropertyListScreen() {
        composeTestRule.setContent() {
            loadViewModel(
                viewModel = PropertyListViewModel(
                    loadPropertiesUseCase = loadPropertiesUseCase,
                    getSelectedCurrencyUseCase = getSelectedCurrencyUseCase,
                    getAvailableCurrenciesUseCase = getAvailableCurrenciesUseCase,
                    saveSelectedCurrencyUseCase = saveSelectedCurrencyUseCase,
                    propertyListMapper = propertyListMapper,
                    routeFactory = routeFactory
                )
            )

            PropertyListScreen(
                onShowSnackBarMessage = { },
                onNavigateTo = { },
                appScope = CoroutineScope(Dispatchers.Unconfined)
            )
        }

//        runTest(testDispatcher) {
//            val item = FakeProperty
//            composeTestRule.onNodeWithTag(
//                testTag = "property_list_card_description_${item.id}"
//            ).assertDoesNotExist()
//
//            composeTestRule
//                .onNodeWithTag(
//                    testTag = "property_list_card_displayPrice_${item.id}"
//                ).assertExists()
//
//            composeTestRule.onNodeWithTag(
//                testTag = "property_list_card_location_${item.id}"
//            ).assertExists()
//
//            composeTestRule.onNodeWithTag(
//                testTag = "property_list_card_name_${item.id}"
//            ).assertExists()
//        }
    }
}