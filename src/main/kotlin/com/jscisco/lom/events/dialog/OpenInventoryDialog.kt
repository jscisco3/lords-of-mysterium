package com.jscisco.lom.events.dialog

import com.jscisco.lom.actor.Player
import com.jscisco.lom.dungeon.Dungeon
import org.hexworks.cobalt.events.api.Event

data class OpenInventoryDialog(val dungeon: Dungeon, val player: Player) : Event