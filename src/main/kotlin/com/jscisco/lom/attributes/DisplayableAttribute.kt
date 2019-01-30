package com.jscisco.lom.attributes

import org.hexworks.amethyst.api.Attribute
import org.hexworks.zircon.api.component.Component

/**
 * Represents an [Attribute] which can be displayed
 * visually as a [Component]
 */
interface DisplayableAttribute : Attribute {

    /**
     * Creates a [Component] with the given `width` which displays
     * this [Attribute]
     */
    fun toComponent(width: Int): Component

}