package com.pedronsouza.feature.property_list

import androidx.lifecycle.viewModelScope
import com.pedronsouza.domain.ObjectMapper
import com.pedronsouza.domain.models.Property
import com.pedronsouza.domain.useCases.LoadPropertiesUseCase
import com.pedronsouza.shared.AppScreen
import com.pedronsouza.shared.Constants
import com.pedronsouza.shared.components.ComponentViewModel
import com.pedronsouza.shared.components.ViewEffect
import com.pedronsouza.shared.components.ViewEvent
import com.pedronsouza.shared.components.ViewState
import com.pedronsouza.shared.components.models.PropertyItem
import com.pedronsouza.shared.mappers.PropertyListMapper
import com.pedronsouza.shared.navigation.RouteFactory
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

data class State(
    val isLoading: Boolean = false,
    val properties: List<PropertyItem> = emptyList()
) : ViewState

sealed class PropertyListEvent : ViewEvent {
    data object LoadProperties : PropertyListEvent()
    data class PropertySelected(val item: PropertyItem) : PropertyListEvent()
}

sealed class PropertyListEffects : ViewEffect {
    data class ShowErrorToast(val textRef: Int) : PropertyListEffects()
    data class NavigateTo(val finalRoute: String) : PropertyListEffects()
}

internal class PropertyListViewModel(
    private val loadPropertiesUseCase: LoadPropertiesUseCase,
    private val propertyListMapper: PropertyListMapper,
    private val routeFactory: RouteFactory
) : ComponentViewModel<PropertyListEvent, State, PropertyListEffects>() {
    private val logTag = "${Constants.LOG_TAG_FEATURE}:PropertyList"

    override fun initialViewState() = State(true)

    override fun processViewEvents(event: PropertyListEvent) {
        when (event) {
            PropertyListEvent.LoadProperties -> loadProperties()
            is PropertyListEvent.PropertySelected -> preparePropertyDetailNavigationParameter(
                event.item
            )
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun preparePropertyDetailNavigationParameter(item: PropertyItem) {
        runCatching {
            routeFactory.createRoute(AppScreen.DETAIL, Json.encodeToString(item))
        }.onSuccess { url ->
            triggerEffect { PropertyListEffects.NavigateTo(url) }
        }.onFailure {
            Timber.tag(logTag).e("preparePropertyDetailNavigationParameter error")
            Timber.tag(logTag).e(it)
            triggerEffect { PropertyListEffects.ShowErrorToast(R.string.something_went_wrong) }
        }
    }

    private fun loadProperties() {
        viewModelScope.launch {
            CoroutineExceptionHandler { _, error ->
                Timber.tag(logTag).e(error)
                updateState {
                    copy(
                        isLoading = false
                    )
                }
            }

            val newProperties = mutableListOf<PropertyItem>()
            val result = withContext(Dispatchers.IO) {
                loadPropertiesUseCase.execute()
            }

            result.onSuccess { properties ->
                Timber.tag(logTag).d("Properties Loaded")

                withContext(Dispatchers.IO) {
                    newProperties.plusAssign(
                        propertyListMapper.transform(properties)
                    )
                }
            }.onFailure { error ->
                Timber.tag(logTag).e("loadProperties error")
                Timber.tag(logTag).e(error)

                triggerEffect {
                    PropertyListEffects.ShowErrorToast(R.string.something_went_wrong)
                }
            }

            updateState {
                copy(
                    properties = newProperties,
                    isLoading = false
                )
            }
        }
    }
}