package com.jscisco.lom.builders

import com.jscisco.lom.blocks.GameBlock

object GameBlockRepository {

    fun floor() = GameBlock.create()

    fun wall() = GameBlock.createWith(EntityFactory.newWall())

}