package com.jscisco.lom.attributes

import com.jscisco.lom.events.scope.EntityEventScope
import org.hexworks.amethyst.api.Attribute
import org.hexworks.cobalt.events.api.Subscription

data class EventListenerAttribute(val scope: EntityEventScope = EntityEventScope(), val eventListeners: MutableList<Subscription> = mutableListOf()) : Attribute