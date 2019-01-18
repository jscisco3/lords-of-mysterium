package com.jscisco.lom.commands

import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.amethyst.api.entity.EntityType

/**
 * A Specialized [GameCommand] which represents an action an entity can perform
 * on a target [GameEntity]
 */
interface EntityAction<S : EntityType, T : EntityType> : GameCommand<S> {

    val target: GameEntity<T>

    operator fun component1() = context
    operator fun component2() = source
    operator fun component3() = target

}