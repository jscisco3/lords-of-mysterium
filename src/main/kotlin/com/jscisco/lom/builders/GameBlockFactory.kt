package com.jscisco.lom.builders

import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.terrain.Wall

object GameBlockFactory {


    fun floor() = GameBlock.create()

    fun wall() = GameBlock.create().also {
        it.terrain = Wall()
    }
}