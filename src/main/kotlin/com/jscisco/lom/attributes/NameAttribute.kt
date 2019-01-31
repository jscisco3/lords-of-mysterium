package com.jscisco.lom.attributes

import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component

data class NameAttribute(val name: String) : DisplayableAttribute {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun toComponent(width: Int): Component {
        return Components.textBox()
                .withContentWidth(width)
                .addParagraph(name, withNewLine = false)
                .build()
    }
}