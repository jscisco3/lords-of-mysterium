package com.jscisco.lom.commands

import com.jscisco.lom.actor.Player
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.item.Item
import org.hexworks.cobalt.datatypes.extensions.ifPresent
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory

class DropItemCommand(dungeon: Dungeon, val player: Player, val item: Item) : Command(dungeon, player) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun invoke(): Response {
        dungeon.fetchBlockAt(player.position).ifPresent {
            player.inventory.items.remove(item)
            item.position = player.position
            it.addItem(item)
            Consumed
        }
        return Pass
    }
}