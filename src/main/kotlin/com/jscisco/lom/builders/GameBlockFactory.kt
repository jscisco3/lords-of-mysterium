package com.jscisco.lom.builders

import com.jscisco.lom.blocks.GameBlock

object GameBlockFactory {


    fun floor() = GameBlock.create()

    fun wall() = GameBlock.createWith(EntityFactory.newWall())

    fun stairsUp() = GameBlock.createWith(EntityFactory.stairsUp())

    fun stairsDown() = GameBlock.createWith(EntityFactory.stairsDown())

}