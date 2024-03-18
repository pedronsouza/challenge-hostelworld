package com.pedronsouza.feature.property_list

import com.pedronsouza.shared_test.MainDispatcherRule
import app.cash.turbine.test
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
import com.pedronsouza.shared.navigation.RouteFactoryImpl
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class PropertyListViewModelTest {
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

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

    private val routeFactory = RouteFactoryImpl()

    @Test
    fun `Given all dependencies are working properly When the LoadProperties event is received THEN it post the expected success State`() {
        runTest(testDispatcher) {
            val subject =
                PropertyListViewModel(
                    loadPropertiesUseCase = loadPropertiesUseCase,
                    getSelectedCurrencyUseCase = getSelectedCurrencyUseCase,
                    getAvailableCurrenciesUseCase = getAvailableCurrenciesUseCase,
                    saveSelectedCurrencyUseCase = saveSelectedCurrencyUseCase,
                    propertyListMapper = propertyListMapper,
                    routeFactory = routeFactory
                )

            subject.sendEvent(PropertyListEvent.LoadProperties)

            subject.viewState.test {
                assertTrue(awaitItem().isLoading)
                val finalViewState = awaitItem()

                assertFalse(finalViewState.isLoading)
                assertEquals(listOf(FakePropertyItem), finalViewState.properties)
                assertEquals(null, finalViewState.error)
                assertEquals(AppCurrency("EUR"), finalViewState.selectedCurrency)
                assertEquals(listOf(AppCurrency("EUR")), finalViewState.availableCurrencies)

                ensureAllEventsConsumed()
            }
        }
    }

    @Test
    fun `Given loadPropertiesUseCase throws an error When the LoadProperties event is received THEN it should post state of error`() {
        val exception = IllegalAccessError()
        val subject = PropertyListViewModel(
            loadPropertiesUseCase = object: LoadPropertiesUseCase {
                override suspend fun execute(): Result<List<Property>> =
                    Result.failure(exception)
            },
            getSelectedCurrencyUseCase = getSelectedCurrencyUseCase,
            getAvailableCurrenciesUseCase = getAvailableCurrenciesUseCase,
            saveSelectedCurrencyUseCase = saveSelectedCurrencyUseCase,
            propertyListMapper = propertyListMapper,
            routeFactory = routeFactory
        )


        runTest(testDispatcher) {
            subject.sendEvent(PropertyListEvent.LoadProperties)
            subject.viewState.test {
                assertTrue(awaitItem().isLoading)
                val finalViewState = awaitItem()

                assertFalse(finalViewState.isLoading)
                assertEquals(exception, finalViewState.error)

                ensureAllEventsConsumed()
            }
        }
    }

    @Test
    fun `Given getSelectedCurrencyUseCase throws an error When the LoadProperties event is received THEN it should post state of error`() {
        val exception = IllegalAccessError()
        val subject = PropertyListViewModel(
            loadPropertiesUseCase = loadPropertiesUseCase,
            getSelectedCurrencyUseCase = object: GetSelectedCurrencyUseCase {
                override suspend fun execute(): Result<AppCurrency> =
                    Result.failure(exception)
            },
            getAvailableCurrenciesUseCase = getAvailableCurrenciesUseCase,
            saveSelectedCurrencyUseCase = saveSelectedCurrencyUseCase,
            propertyListMapper = propertyListMapper,
            routeFactory = routeFactory
        )


        runTest(testDispatcher) {
            subject.sendEvent(PropertyListEvent.LoadProperties)
            subject.viewState.test {
                assertTrue(awaitItem().isLoading)
                val finalViewState = awaitItem()

                assertFalse(finalViewState.isLoading)
                assertEquals(exception, finalViewState.error)

                ensureAllEventsConsumed()
            }
        }
    }

    @Test
    fun `Given getAvailableCurrenciesUseCase throws an error When the LoadProperties event is received THEN it should post state of error`() {
        val exception = IllegalAccessError()
        val subject = PropertyListViewModel(
            loadPropertiesUseCase = loadPropertiesUseCase,
            getSelectedCurrencyUseCase = getSelectedCurrencyUseCase,
            getAvailableCurrenciesUseCase = object: GetAvailableCurrenciesUseCase {
                override suspend fun execute(): Result<List<AppCurrency>> =
                    Result.failure(exception)

            },
            saveSelectedCurrencyUseCase = saveSelectedCurrencyUseCase,
            propertyListMapper = propertyListMapper,
            routeFactory = routeFactory
        )


        runTest(testDispatcher) {
            subject.sendEvent(PropertyListEvent.LoadProperties)
            subject.viewState.test {
                assertTrue(awaitItem().isLoading)
                val finalViewState = awaitItem()

                assertFalse(finalViewState.isLoading)
                assertEquals(exception, finalViewState.error)

                ensureAllEventsConsumed()
            }
        }
    }

    @Test
    fun `Given saveSelectedCurrencyUseCase returns an error When the SwitchCurrency event is received THEN it should post the current configuration`() {
        val exception = IllegalAccessError()
        val subject = PropertyListViewModel(
            loadPropertiesUseCase = loadPropertiesUseCase,
            getSelectedCurrencyUseCase = getSelectedCurrencyUseCase,
            getAvailableCurrenciesUseCase = getAvailableCurrenciesUseCase,
            saveSelectedCurrencyUseCase = object: SaveSelectedCurrencyUseCase {
                override fun execute(currency: AppCurrency): Result<Unit> =
                    Result.failure(exception)
            },
            propertyListMapper = propertyListMapper,
            routeFactory = routeFactory
        )

        runTest(testDispatcher) {
            val expectedCurrency = AppCurrency("USD")

            subject.sendEvent(PropertyListEvent.SwitchCurrency(AppCurrency("USD")))

            subject.viewState.test {
                val finalViewState = awaitItem()

                assertFalse(finalViewState.isLoading)
                assertEquals(expectedCurrency, finalViewState.selectedCurrency)

                ensureAllEventsConsumed()
            }
        }
    }

    @Test
    fun `Given user interacts with a property When the PropertySelected event is received THEN it should post NavigateTo event with expect route`() {
        val expectedRoute = "/expected_route"
        val subject = PropertyListViewModel(
            loadPropertiesUseCase = loadPropertiesUseCase,
            getSelectedCurrencyUseCase = getSelectedCurrencyUseCase,
            getAvailableCurrenciesUseCase = getAvailableCurrenciesUseCase,
            saveSelectedCurrencyUseCase = saveSelectedCurrencyUseCase,
            propertyListMapper = propertyListMapper,
            routeFactory = object: RouteFactory {
                override fun createRoute(screen: AppScreen, parameter: String?): String =
                    expectedRoute
            }
        )

        runTest(testDispatcher) {
            val expectedProperty = FakePropertyItem

            subject.sendEvent(PropertyListEvent.PropertySelected(expectedProperty))
            subject.viewEffect.test {
                val newEffect = awaitItem()

                assertTrue { newEffect is PropertyListEffects.NavigateTo }
                assertEquals(
                    PropertyListEffects.NavigateTo(expectedRoute),
                    newEffect
                )

                ensureAllEventsConsumed()
            }
        }
    }

    @Test
    fun `Given user interacts with a property When the PropertySelected event is received AND routefactory fails to give a route THEN it should trigger a toast message effect`() {
        val expectedError = IllegalAccessError()
        val subject = PropertyListViewModel(
            loadPropertiesUseCase = loadPropertiesUseCase,
            getSelectedCurrencyUseCase = getSelectedCurrencyUseCase,
            getAvailableCurrenciesUseCase = getAvailableCurrenciesUseCase,
            saveSelectedCurrencyUseCase = saveSelectedCurrencyUseCase,
            propertyListMapper = propertyListMapper,
            routeFactory = object: RouteFactory {
                override fun createRoute(screen: AppScreen, parameter: String?): String =
                    throw expectedError
            }
        )

        runTest(testDispatcher) {
            val expectedProperty = FakePropertyItem

            subject.sendEvent(PropertyListEvent.PropertySelected(expectedProperty))
            subject.viewEffect.test {
                val newEffect = awaitItem()

                assertTrue { newEffect is PropertyListEffects.ShowErrorToast }
                ensureAllEventsConsumed()
            }
        }
    }
}