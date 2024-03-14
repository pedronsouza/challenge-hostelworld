package com.pedronsouza.feature.property_detail

import androidx.lifecycle.viewModelScope
import com.pedronsouza.shared.Constants
import com.pedronsouza.shared.components.ComponentViewModel
import com.pedronsouza.shared.components.ViewEffect
import com.pedronsouza.shared.components.ViewEvent
import com.pedronsouza.shared.components.ViewState
import com.pedronsouza.shared.components.models.PropertyItem
import kotlinx.coroutines.launch
import timber.log.Timber

data class State(
    val isLoading: Boolean = false,
    val propertyItem: PropertyItem? = null
) : ViewState

sealed class PropertyDetailEvent : ViewEvent {
    data object PreparePropertyData : PropertyDetailEvent()
}
sealed class PropertyDetailEffect : ViewEffect

class PropertyDetailViewModel(
    private val property: PropertyItem
) :
    ComponentViewModel<PropertyDetailEvent, State, PropertyDetailEffect>() {
    private val logTag = "${Constants.LOG_TAG_FEATURE}:PropertyDetail"

    override fun initialViewState() = State(isLoading = true)

    override fun processViewEvents(event: PropertyDetailEvent) {
        when(event) {
            is PropertyDetailEvent.PreparePropertyData -> loadProperty()
        }
    }

    private fun loadProperty() {
        viewModelScope.launch {
            Timber.tag(logTag).d("Load Property Details\nProperty: $property")

            updateState {
                copy(
                    isLoading = false,
                    propertyItem = property
                )
            }
        }
    }
}