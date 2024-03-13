package com.pedronsouza.feature.property_list

import com.pedronsouza.shared.ComponentViewModel
import com.pedronsouza.shared.ViewEffect
import com.pedronsouza.shared.ViewEvent
import com.pedronsouza.shared.ViewState

data class State(
    val isLoading: Boolean = false
) : ViewState

sealed class PropertyListEvent : ViewEvent {

}

sealed class PropertyListEffects : ViewEffect {

}

class PropertyListViewModel : ComponentViewModel<PropertyListEvent, State, PropertyListEffects>() {
    override fun initialViewState(): State { TODO("Not yet implemented") }
    override fun processViewEvents(event: PropertyListEvent) { TODO("Not yet implemented") }
}