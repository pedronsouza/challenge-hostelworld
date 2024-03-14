package com.pedronsouza.feature.property_detail

import com.pedronsouza.domain.models.Property
import com.pedronsouza.shared.components.ComponentViewModel
import com.pedronsouza.shared.components.ViewEffect
import com.pedronsouza.shared.components.ViewEvent
import com.pedronsouza.shared.components.ViewState

data class State(
    val isLoading: Boolean = false,
    val property: Property? = null
) : ViewState

sealed class PropertyDetailEvent : ViewEvent {
    data class LoadPropertyWithId(val id: String) : PropertyDetailEvent()
}
sealed class PropertyDetailEffect : ViewEffect

class PropertyDetailViewModel :
    ComponentViewModel<PropertyDetailEvent, State, PropertyDetailEffect>() {
    override fun initialViewState() = State(isLoading = true)

    override fun processViewEvents(event: PropertyDetailEvent) {
        when(event) {
            is PropertyDetailEvent.LoadPropertyWithId -> loadPropertyWithId(
                propertyId = event.id
            )
        }
    }

    private fun loadPropertyWithId(propertyId: String) {

    }
}