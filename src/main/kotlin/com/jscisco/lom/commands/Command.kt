package com.jscisco.lom.commands

import com.jscisco.lom.actor.Actor
import com.jscisco.lom.dungeon.Dungeon

abstract class Command(val dungeon: Dungeon, val receiver: Actor) {

    abstract fun invoke()

}