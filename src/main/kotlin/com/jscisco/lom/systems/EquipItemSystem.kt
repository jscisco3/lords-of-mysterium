package com.jscisco.lom.systems

import com.jscisco.lom.attributes.types.equipment
import com.jscisco.lom.attributes.types.equippable
import com.jscisco.lom.attributes.types.inventory
import com.jscisco.lom.commands.EquipItemCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.GameCommand
import com.jscisco.lom.extensions.responseWhenCommandIs
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object EquipItemSystem : BaseFacet<GameContext>() {
    override fun executeCommand(command: GameCommand<out EntityType>): Response = command.responseWhenCommandIs<EquipItemCommand> { (_, source, item) ->
        source.equipment.equipItemToSlot(source.inventory,
                source.equipment.getSlotsByType(item.equippable.equipmentType).last(),
                item)
        Consumed
    }
}