package com.jscisco.lom.systems

import com.jscisco.lom.dungeon.Context
import org.hexworks.cobalt.datatypes.Identifier

abstract class BaseSystem : System {
    override val id = Identifier.randomIdentifier()

    override fun update(context: Context) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}