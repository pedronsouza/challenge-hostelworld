package com.pedronsouza.feature.property_list

import androidx.lifecycle.viewModelScope
import com.pedronsouza.domain.useCases.LoadPropertiesUseCase
import com.pedronsouza.shared.ComponentViewModel
import com.pedronsouza.shared.ViewEffect
import com.pedronsouza.shared.ViewEvent
import com.pedronsouza.shared.ViewState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

data class State(
    val isLoading: Boolean = false,
    val properties: List<PropertyListItem> = emptyList()
) : ViewState

sealed class PropertyListEvent : ViewEvent {
    data object LoadProperties : PropertyListEvent()
}

sealed class PropertyListEffects : ViewEffect {
    data class ShowErrorToast(val textRef: Int?) : PropertyListEffects()
}

internal class PropertyListViewModel(
    private val loadPropertiesUseCase: LoadPropertiesUseCase,
    private val propertyListMapper: PropertyListItemObjectMapper
) : ComponentViewModel<PropertyListEvent, State, PropertyListEffects>() {
    override fun initialViewState() = State(true)

    override fun processViewEvents(event: PropertyListEvent) {
        when (event) {
            PropertyListEvent.LoadProperties -> {
                viewModelScope.launch {
                    CoroutineExceptionHandler { _, error ->
                        Timber.tag(this::class.simpleName.orEmpty()).e(error)
                        updateState {
                            copy(
                                isLoading = false
                            )
                        }
                    }

                    val newProperties = mutableListOf<PropertyListItem>()
                    val result = withContext(Dispatchers.IO) {
                        loadPropertiesUseCase.execute()
                    }

                    result.onSuccess { properties ->
                        withContext(Dispatchers.IO) {
                            newProperties.plusAssign(
                                propertyListMapper.transform(properties)
                            )
                        }
                    }.onFailure { error ->
                        Timber.tag(this::class.simpleName.orEmpty()).e(error)

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
    }
}