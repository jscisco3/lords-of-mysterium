package com.jscisco.lom.behaviors.ai

import com.jscisco.lom.attributes.ai.HunterSeekerTarget
import com.jscisco.lom.commands.MoveCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.entityName
import com.jscisco.lom.extensions.position
import com.jscisco.lom.extensions.whenHasAttribute
import org.hexworks.amethyst.api.base.BaseBehavior
import org.hexworks.amethyst.api.entity.Entity
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.impl.Position3D
import squidpony.squidmath.AStarSearch

class HunterSeekerAI : BaseBehavior<GameContext>(HunterSeekerTarget::class) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun update(entity: Entity<EntityType, GameContext>, context: GameContext): Boolean {
        // Assume we have a target
        var targetFound = true
        entity.whenHasAttribute<HunterSeekerTarget> {
            if (it.entity == null) {
                it.entity = lookForTarget(context)
                logger.info("Found target: %s.".format(it.entity?.entityName))
                targetFound = it.entity != null
            }
        }
        if (targetFound) {
            val aStarMap = Array(context.dungeon.actualSize().xLength, { DoubleArray(context.dungeon.actualSize().yLength) }).apply {
                context.dungeon.fetchBlocksAtLevel(entity.position.z).forEach {
                    this[it.position.x][it.position.y] = if (it.block.isOccupied) 1.0 else 0.0
                }
            }
            aStarMap[context.dungeon.player.position.x][context.dungeon.player.position.y] = 0.0
            val search = AStarSearch(aStarMap, AStarSearch.SearchType.CHEBYSHEV)
            val coord = search.path(entity.position.x, entity.position.y, context.dungeon.player.position.x, context.dungeon.player.position.y).remove()
            entity.executeCommand(MoveCommand(context,
                    entity,
                    Position3D.create(coord.x, coord.y, entity.position.z)))
        }
        return targetFound
    }

    private fun lookForTarget(context: GameContext): GameEntity<EntityType>? {
        return context.player
    }
}