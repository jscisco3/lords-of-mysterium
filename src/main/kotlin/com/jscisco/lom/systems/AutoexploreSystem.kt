package com.jscisco.lom.systems

import com.jscisco.lom.attributes.AutoexploreAttribute
import com.jscisco.lom.commands.AutoexploreCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.attribute
import com.jscisco.lom.extensions.position
import com.jscisco.lom.extensions.responseWhenCommandIs
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory

object AutoexploreSystem : BaseFacet<GameContext>() {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun executeCommand(command: GameCommand<out EntityType>): Response = command.responseWhenCommandIs<AutoexploreCommand> {
        val autoexploreAttribute = it.source.attribute<AutoexploreAttribute>()
        autoexploreAttribute.costMap = calculateCostMap(it.context, it.source.position.z)
        Consumed
    }

    private fun calculateCostMap(context: GameContext, zlevel: Int): Array<DoubleArray> {
        val dungeon = context.dungeon
        val costMap = Array(dungeon.actualSize().xLength, { DoubleArray(dungeon.actualSize().yLength) })
        dungeon.fetchBlocksAtLevel(zlevel).forEach {
            if (!it.block.seen) {
                costMap[it.position.x][it.position.y] = 0.0
            } else {
                costMap[it.position.x][it.position.y] = 1.0
            }
        }
        return costMap
    }
}