package com.jscisco.lom.attributes

import com.jscisco.lom.commands.EntityAction
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.amethyst.api.Attribute
import org.hexworks.amethyst.api.entity.EntityType
import kotlin.reflect.KClass

/**
 * This [Attribute] represents all the possible [EntityAction]s an entity can make.
 */
class EntityActions(private vararg val actions: KClass<out EntityAction<out EntityType, out EntityType>>) : Attribute {

    /**
     * Creates the possible actions for the given source and target entity
     */
    fun createActionsFor(context: GameContext, source: GameEntity<EntityType>, target: GameEntity<EntityType>):
            Iterable<EntityAction<out EntityType, out EntityType>> {
        return actions.map {
            try {
                it.constructors.first().call(context, source, target)
            } catch (e: Exception) {
                throw IllegalArgumentException("Can't create EntityAction. Does it have proper constructor?")
            }
        }
    }
}