package com.jscisco.lom.attributes

import org.hexworks.amethyst.api.Attribute

data class Openable(var open: Boolean = false) : Attribute {

    fun toggle() {
        open = !open
    }

}