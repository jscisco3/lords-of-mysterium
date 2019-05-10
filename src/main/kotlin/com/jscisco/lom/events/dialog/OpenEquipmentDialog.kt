package com.jscisco.lom.events.dialog

import com.jscisco.lom.actor.Player
import org.hexworks.cobalt.events.api.Event

data class OpenEquipmentDialog(val player: Player) : Event