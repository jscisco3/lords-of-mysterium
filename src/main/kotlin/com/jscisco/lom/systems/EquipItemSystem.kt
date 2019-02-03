package com.jscisco.lom.systems

import com.jscisco.lom.attributes.StatBlockAttribute
import com.jscisco.lom.attributes.TriggersAttribute
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
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory

object EquipItemSystem : BaseFacet<GameContext>() {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

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
        equipmentSlot.equippedItem.whenHasAttribute<StatBlockAttribute> {
            source.statBlock + it
        }
        equipmentSlot.equippedItem.whenHasAttribute<TriggersAttribute> {
            it.triggers.forEach { trigger ->
                trigger.activate()
            }
        }
        Consumed
    }
}