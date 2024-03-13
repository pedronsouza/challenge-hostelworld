package com.pedronsouza.feature.property_list

import com.pedronsouza.shared.ComponentViewModel
import com.pedronsouza.shared.ViewEffect
import com.pedronsouza.shared.ViewEvent
import com.pedronsouza.shared.ViewState

data class State(
    val isLoading: Boolean = false
) : ViewState

sealed class PropertyListEvent : ViewEvent {
    data object LoadProperties : PropertyListEvent()
}

sealed class PropertyListEffects : ViewEffect {

}

class PropertyListViewModel : ComponentViewModel<PropertyListEvent, State, PropertyListEffects>() {
    override fun initialViewState() = State(true)

    override fun processViewEvents(event: PropertyListEvent) {
        when (event) {
            PropertyListEvent.LoadProperties -> {

            }
        }
    }
}