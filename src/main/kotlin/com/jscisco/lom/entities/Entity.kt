package com.jscisco.lom.entities

import com.jscisco.lom.entities.attributes.Attribute
import com.jscisco.lom.entities.attributes.EntityMetadata
import org.hexworks.cobalt.datatypes.Identifier
import org.hexworks.cobalt.datatypes.Maybe

interface Entity {

    val id: Identifier
    val metadata: EntityMetadata

    fun <T : Attribute> attribute(klass: Class<T>): Maybe<T>


}