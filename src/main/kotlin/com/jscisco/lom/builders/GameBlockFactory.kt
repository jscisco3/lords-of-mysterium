package com.jscisco.lom.builders

import com.jscisco.lom.blocks.GameBlock

object GameBlockFactory {


    fun floor() = GameBlock.createWith(EntityFactory.newFloor())

    fun wall() = GameBlock.createWith(EntityFactory.newWall())

}