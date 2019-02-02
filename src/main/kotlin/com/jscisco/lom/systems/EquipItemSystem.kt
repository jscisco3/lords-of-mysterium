package com.jscisco.lom.systems

import com.jscisco.lom.attributes.StatBlockAttribute
import com.jscisco.lom.attributes.types.inventory
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.commands.EquipItemCommand
import com.jscisco.lom.commands.UnequipItemCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.*
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType

object EquipItemSystem : BaseFacet<GameContext>() {
    override fun executeCommand(command: GameCommand<out EntityType>): Response = command.responseWhenCommandIs<EquipItemCommand> { (context, source, item, equipmentSlot) ->
        val oldItem = equipmentSlot.equippedItem
        source.inventory.removeItem(item)
        if (oldItem.entityName == EntityFactory.NO_EQUIPMENT.entityName) {
            equipmentSlot.equippedItem = item
        } else {
            source.executeCommand(UnequipItemCommand(
                    context = context,
                    source = source,
                    equipmentSlot = equipmentSlot
            ))
            equipmentSlot.equippedItemProperty.value = item
        }
        if (equipmentSlot.equippedItem.hasAttribute<StatBlockAttribute>()) {
            source.statBlock + equipmentSlot.equippedItem.statBlock
        }
        Consumed
    }
}