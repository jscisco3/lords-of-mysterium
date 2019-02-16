package com.jscisco.lom.attributes

import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.events.api.EventScope

data class EventListenerAttribute(val scope: EventScope) : Attribute