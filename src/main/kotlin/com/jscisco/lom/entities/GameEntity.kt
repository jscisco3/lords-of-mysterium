package com.jscisco.lom.entities;

import com.jscisco.lom.entities.attributes.Attribute
import com.jscisco.lom.entities.attributes.EntityMetadata
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.factory.IdentifierFactory

class GameEntity(override val metadata: EntityMetadata,
                 val attributes: Set<Attribute> = setOf())
    : Entity {

    override val id = IdentifierFactory.randomIdentifier()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Attribute> attribute(klass: Class<T>): Maybe<T> {
        return Maybe.ofNullable(attributes.firstOrNull { klass.isInstance(it) } as? T)
    }

    override fun toString(): String {
        return metadata.name
    }

}
