package com.jscisco.lom.builders

import com.jscisco.lom.blocks.GameBlock

object GameBlockFactory {


    fun floor() = GameBlock.create()

    fun wall() = GameBlock.createWith(EntityFactory.newWall())

    fun closedDoor() = GameBlock.createWith(EntityFactory.newClosedDoor())

    fun openDoor() = GameBlock.createWith(EntityFactory.newOpenDoor())
}