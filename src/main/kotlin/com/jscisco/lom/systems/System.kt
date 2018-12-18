package com.jscisco.lom.systems

import com.jscisco.lom.dungeon.Context
import org.hexworks.cobalt.datatypes.Identifier

interface System {

    val id: Identifier

    fun update(context: Context)

//    fun executeCommand()
}