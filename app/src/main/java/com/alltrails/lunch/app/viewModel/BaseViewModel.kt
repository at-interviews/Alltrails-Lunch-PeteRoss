package com.alltrails.lunch.app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn

abstract class BaseViewModel<StateType, ActionType, UIEventType>(
  private val initialState: StateType
) : ViewModel() {

  val viewState: StateFlow<StateType> get() = state
  abstract fun handleUIEvent(event: UIEventType)

  protected val state: IStateReducer<StateType, ActionType> by lazy {
    StateReducer(initialState, ::reducer, viewModelScope)
  }

  protected abstract fun reducer(state: StateType, action:ActionType): StateType
}

interface IStateReducer<StateType, ActionType> : StateFlow<StateType> {
  fun dispatch(action: ActionType)
}

class StateReducer<StateType, ActionType>(
  initialState: StateType,
  reduceState: (StateType, ActionType) -> StateType,
  scope: CoroutineScope,
) : IStateReducer<StateType, ActionType> {

  private val actions = Channel<ActionType>()

  private val stateFlow = actions
    .receiveAsFlow()
    .runningFold(initialState, reduceState)
    .stateIn(scope, Eagerly, initialState)

  override val replayCache: List<StateType>
    get() = stateFlow.replayCache

  override suspend fun collect(collector: FlowCollector<StateType>): Nothing {
    stateFlow.collect(collector)
  }

  override val value: StateType
    get() = stateFlow.value

  override fun dispatch(action: ActionType) {
    actions.trySend(action)
  }
}