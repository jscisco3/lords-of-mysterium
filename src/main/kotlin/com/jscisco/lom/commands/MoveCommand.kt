package com.jscisco.lom.commands

import GameCommand
import GameEntity
import com.jscisco.lom.dungeon.GameContext
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.zircon.api.data.impl.Position3D

data class MoveCommand(override val context: GameContext,
                       override val source: GameEntity<EntityType>,
                       val position: Position3D) : GameCommand<EntityType>
//
//    private val logger: Logger = LoggerFactory.getLogger(javaClass)
//
//    override fun invoke(): Response {
//        var response: Response = Pass

//    }
//}