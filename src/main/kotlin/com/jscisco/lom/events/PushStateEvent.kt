package com.jscisco.lom.events

import com.jscisco.lom.dungeon.state.State
import org.hexworks.cobalt.events.api.Event

data class PushStateEvent(val state: State) : Event