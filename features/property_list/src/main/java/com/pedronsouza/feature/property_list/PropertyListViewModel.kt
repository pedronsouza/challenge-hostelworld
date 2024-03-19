package com.pedronsouza.feature.property_list

import androidx.lifecycle.viewModelScope
import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.useCases.GetAvailableCurrenciesUseCase
import com.pedronsouza.domain.useCases.GetSelectedCurrencyUseCase
import com.pedronsouza.domain.useCases.LoadPropertiesUseCase
import com.pedronsouza.domain.useCases.SaveSelectedCurrencyUseCase
import com.pedronsouza.domain.values.AppCurrency
import com.pedronsouza.shared.AppScreen
import com.pedronsouza.shared.Constants
import com.pedronsouza.shared.components.ComponentViewModel
import com.pedronsouza.shared.components.ViewEffect
import com.pedronsouza.shared.components.ViewEvent
import com.pedronsouza.shared.components.ViewState
import com.pedronsouza.shared.components.models.PropertyItem
import com.pedronsouza.shared.extensions.getRootCause
import com.pedronsouza.shared.mappers.PropertyListMapper
import com.pedronsouza.shared.navigation.RouteFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import com.pedronsouza.shared.R
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

data class State(
    val isLoading: Boolean = false,
    val properties: List<PropertyItem> = emptyList(),
    val error: Throwable? = null,
    val selectedCurrency: AppCurrency? = null,
    val availableCurrencies: List<AppCurrency> = emptyList()
) : ViewState

sealed class PropertyListEvent : ViewEvent {
    data object LoadProperties : PropertyListEvent()
    data class SwitchCurrency(val newCurrency: AppCurrency) : PropertyListEvent()
    data class PropertySelected(val item: PropertyItem) : PropertyListEvent()
}

sealed class PropertyListEffects : ViewEffect {
    data class ShowErrorToast(val textRef: Int) : PropertyListEffects()
    data class NavigateTo(val finalRoute: String) : PropertyListEffects()
}

internal class PropertyListViewModel(
    private val loadPropertiesUseCase: LoadPropertiesUseCase,
    private val getSelectedCurrencyUseCase: GetSelectedCurrencyUseCase,
    private val getAvailableCurrenciesUseCase: GetAvailableCurrenciesUseCase,
    private val saveSelectedCurrencyUseCase: SaveSelectedCurrencyUseCase,
    private val propertyListMapper: PropertyListMapper,
    private val routeFactory: RouteFactory
) : ComponentViewModel<PropertyListEvent, State, PropertyListEffects>() {
    private val logTag = "${Constants.LOG_TAG_FEATURE}:PropertyList"

    override fun initialViewState() = State(
        isLoading = true,
        properties = emptyList(),
        error = null,
        selectedCurrency = null,
        availableCurrencies = emptyList()
    )

    override fun processViewEvents(event: PropertyListEvent) {
        when (event) {
            PropertyListEvent.LoadProperties -> loadProperties()

            is PropertyListEvent.SwitchCurrency -> switchCurrency(event.newCurrency)

            is PropertyListEvent.PropertySelected -> preparePropertyDetailNavigationParameter(
                event.item
            )
        }
    }

    private fun switchCurrency(newCurrency: AppCurrency) {
        var selectedCurrency = viewState.value.selectedCurrency

        saveSelectedCurrencyUseCase
            .execute(newCurrency)
            .onSuccess {
                Timber.tag(logTag).d("Currency switched: $newCurrency")
                selectedCurrency = newCurrency
            }.onFailure { error ->
                Timber.tag(logTag).e("Fails to switch currency")
                Timber.tag(logTag).e(error)
            }

        updateState {
            initialViewState().copy(
                availableCurrencies = availableCurrencies,
                selectedCurrency = selectedCurrency
            )
        }

        viewModelScope.launch {
            Timber.tag(logTag).d("")
            loadPropertiesUseCase.execute().onSuccess { properties ->
                updateState {
                    copy(
                        selectedCurrency = newCurrency,
                        properties = propertyListMapper.transform(properties),
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun preparePropertyDetailNavigationParameter(item: PropertyItem) {
        runCatching {
            routeFactory.createRoute(AppScreen.DETAIL, listOf(item.id, item.name))
        }.onSuccess { url ->
            triggerEffect { PropertyListEffects.NavigateTo(url) }
        }.onFailure {
            Timber.tag(logTag).e("preparePropertyDetailNavigationParameter error")
            Timber.tag(logTag).e(it)
            triggerEffect { PropertyListEffects.ShowErrorToast(R.string.something_went_wrong) }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun loadProperties() {
        viewModelScope.launch {
            CoroutineExceptionHandler { _, error ->
                Timber.tag(logTag).e(error)
                updateState {
                    copy(
                        isLoading = false,
                        error = error.getRootCause()
                    )
                }
            }

            updateState { initialViewState() }

            val newProperties = mutableListOf<PropertyItem>()
            var newError = viewState.value.error
            var newCurrency = viewState.value.selectedCurrency
            var newAvailableCurrencies = viewState.value.availableCurrencies

            val deferredResults = withContext(Dispatchers.IO) {
                listOf(
                    async { loadPropertiesUseCase.execute() },
                    async { getSelectedCurrencyUseCase.execute() },
                    async { getAvailableCurrenciesUseCase.execute() }
                )
            }

            val results = awaitAll(*deferredResults.toTypedArray())

            val loadPropertiesResult = results[0] as Result<List<Property>>
            val selectedCurrencyResult = results[1] as Result<AppCurrency>
            val availableCurrenciesResult = results[2] as Result<List<AppCurrency>>

            availableCurrenciesResult.onSuccess { currencies ->
                Timber.tag(logTag).d("Available Currencies Loaded: $currencies")
                newAvailableCurrencies = currencies
            }.onFailure { error ->
                Timber.tag(logTag).e(error)
                Timber.tag(logTag).e(error)

                newError = error.getRootCause()
            }

            selectedCurrencyResult.onSuccess { currency ->
                Timber.tag(logTag).d("Selected Currency Loaded: $currency")
                newCurrency = currency
            }.onFailure { error ->
                Timber.tag(logTag).e("Error retrieving the selected currency")
                Timber.tag(logTag).e(error)

                newError = error.getRootCause()
            }

            loadPropertiesResult.onSuccess { properties ->
                Timber.tag(logTag).d("Properties Loaded")

                withContext(Dispatchers.IO) {
                    newProperties.plusAssign(
                        propertyListMapper.transform(properties)
                    )
                }
            }.onFailure { error ->
                Timber.tag(logTag).e("loadProperties error")
                Timber.tag(logTag).e(error)

                newError = error.getRootCause()

                triggerEffect {
                    PropertyListEffects.ShowErrorToast(R.string.something_went_wrong)
                }
            }

            updateState {
                copy(
                    properties = newProperties,
                    isLoading = false,
                    error = newError,
                    selectedCurrency = newCurrency,
                    availableCurrencies = newAvailableCurrencies
                )
            }
        }
    }
}