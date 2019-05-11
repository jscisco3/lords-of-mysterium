package com.jscisco.lom.attributes

import org.hexworks.zircon.api.component.Component

interface DisplayableAttribute {

    fun toComponent(width: Int): Component

}