package com.jscisco.lom.attributes

import com.jscisco.lom.trigger.Trigger
import org.hexworks.amethyst.api.Attribute

data class TriggersAttribute(val triggers: MutableList<Trigger>) : Attribute