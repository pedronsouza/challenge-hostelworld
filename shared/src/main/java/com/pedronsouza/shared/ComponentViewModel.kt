package com.pedronsouza.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface ViewState
interface ViewEvent
interface ViewEffect

abstract class ComponentViewModel<Event : ViewEvent, State : ViewState, Effect : ViewEffect> : ViewModel() {
    private val _viewEvent: MutableSharedFlow<Event> = MutableSharedFlow()
    val viewEvent = _viewEvent.asSharedFlow()

    private val _viewState: MutableStateFlow<State> by lazy { MutableStateFlow(initialViewState()) }
    val viewState by lazy { _viewState.asStateFlow() }

    private val _viewEffect = Channel<Effect>()
    val viewEffect = _viewEffect.receiveAsFlow()

    abstract fun initialViewState(): State

    init {
        listenToEvents()
    }

    private fun listenToEvents() {
        viewModelScope.launch {
            viewEvent.collect { processViewEvents(it) }
        }
    }

    fun sendEvent(event: Event) {
        viewModelScope.launch { _viewEvent.emit(event) }
    }

    protected fun updateState(reduce: State.() -> State) {
        _viewState.update { _viewState.value.reduce() }
    }

    protected fun triggerEffect(builder: () -> Effect) {
        viewModelScope.launch { _viewEffect.send(builder()) }
    }

    abstract fun processViewEvents(event: Event)
}