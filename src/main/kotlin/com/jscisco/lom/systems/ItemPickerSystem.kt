package com.jscisco.lom.systems

import com.jscisco.lom.attributes.types.inventory
import com.jscisco.lom.commands.PickItemUp
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.responseWhenCommandIs
import com.jscisco.lom.extensions.whenHasItems
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object ItemPickerSystem : BaseFacet<GameContext>() {

    override fun executeCommand(command: GameCommand<out EntityType>) = command.responseWhenCommandIs<PickItemUp> { (context, itemHolder, position) ->
        val dungeon = context.dungeon
        dungeon.findItemsAt(position).whenHasItems { items ->
            val item = items.last()
            itemHolder.inventory.addItem(item)
            dungeon.removeEntity(item)
        }
        Consumed
    }

}