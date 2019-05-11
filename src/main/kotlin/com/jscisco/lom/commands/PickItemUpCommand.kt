package com.jscisco.lom.commands

import com.jscisco.lom.actor.Player
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.item.Item
import org.hexworks.cobalt.datatypes.extensions.ifPresent
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.data.impl.Position3D

class PickItemUpCommand(dungeon: Dungeon, val player: Player, private val position: Position3D) : Command(dungeon, player) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun invoke(): Response {
        dungeon.fetchBlockAt(position).ifPresent {
            if (it.items.isEmpty().not()) {
                val item: Item = it.items[0]
                it.removeItem(item)
                player.inventory.items.add(item)
                Consumed
            }
        }
        return Pass
    }
}