package com.jscisco.lom.systems

import com.jscisco.lom.attributes.types.inventory
import com.jscisco.lom.commands.DropItem
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.responseWhenCommandIs
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory

object ItemDropper : BaseFacet<GameContext>() {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun executeCommand(command: GameCommand<out EntityType>) = command.responseWhenCommandIs<DropItem> { (context, itemHolder, item, position) ->
        if (itemHolder.inventory.removeItem(item)) {
            context.dungeon.addEntity(item, position)
        }
        Consumed
    }
}