package com.pedronsouza.feature.property_detail

import androidx.lifecycle.viewModelScope
import com.pedronsouza.domain.ObjectMapper
import com.pedronsouza.domain.models.Property
import com.pedronsouza.shared.components.ComponentViewModel
import com.pedronsouza.shared.components.ViewEffect
import com.pedronsouza.shared.components.ViewEvent
import com.pedronsouza.shared.components.ViewState
import com.pedronsouza.shared.components.models.PropertyListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class State(
    val isLoading: Boolean = false,
    val propertyItem: PropertyListItem? = null
) : ViewState

sealed class PropertyDetailEvent : ViewEvent {
    data object PreparePropertyData : PropertyDetailEvent()
}
sealed class PropertyDetailEffect : ViewEffect

class PropertyDetailViewModel(
    private val property: Property,
    private val propertyListItemMapper: ObjectMapper<List<Property>, List<PropertyListItem>>
) :
    ComponentViewModel<PropertyDetailEvent, State, PropertyDetailEffect>() {
    override fun initialViewState() = State(isLoading = true)

    override fun processViewEvents(event: PropertyDetailEvent) {
        when(event) {
            is PropertyDetailEvent.PreparePropertyData -> prepareDataForView()
        }
    }

    private fun prepareDataForView() {
        viewModelScope.launch {
            val propertyItem = withContext(Dispatchers.IO) {
                propertyListItemMapper.transform(listOf(property)).first() // TODO: smell
            }

            updateState {
                copy(
                    isLoading = false,
                    propertyItem = propertyItem
                )
            }
        }
    }
}