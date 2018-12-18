package com.jscisco.lom.entities

import com.jscisco.lom.entities.attributes.EntityMetadata
import org.hexworks.cobalt.datatypes.Identifier

interface Entity {

    val id: Identifier
    val metadata: EntityMetadata

}