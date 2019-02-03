package com.jscisco.lom.systems

import com.jscisco.lom.attributes.StatBlockAttribute
import com.jscisco.lom.attributes.TriggersAttribute
import com.jscisco.lom.attributes.types.Item
import com.jscisco.lom.attributes.types.inventory
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.commands.UnequipItemCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.*
import org.hexworks.amethyst.api.Consumed
import org.hexworks.amethyst.api.Response
import org.hexworks.amethyst.api.base.BaseFacet
import org.hexworks.amethyst.api.entity.EntityType
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory

object UnequipItemSystem : BaseFacet<GameContext>() {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun executeCommand(command: GameCommand<out EntityType>): Response = command.responseWhenCommandIs<UnequipItemCommand> { (_, source, equipmentSlot) ->
        val oldItem: GameEntity<Item> = equipmentSlot.equippedItem
        if (oldItem.entityName != EntityFactory.NO_EQUIPMENT.entityName) {
            source.inventory.addItem(oldItem)
            equipmentSlot.equippedItem = EntityFactory.NO_EQUIPMENT

            oldItem.whenHasAttribute<StatBlockAttribute> {
                source.statBlock - it
            }
            oldItem.whenHasAttribute<TriggersAttribute> {
                it.triggers.forEach { trigger ->
                    trigger.deactivate()
                }
            }
        }
        Consumed
    }
}