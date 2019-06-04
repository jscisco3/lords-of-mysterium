package com.jscisco.lom.builders

import com.jscisco.lom.blocks.GameBlock
import com.jscisco.lom.terrain.ClosedDoor
import com.jscisco.lom.terrain.OpenDoor
import com.jscisco.lom.terrain.Wall

object GameBlockFactory {


    fun floor() = GameBlock.create()

    fun wall() = GameBlock.createWith(EntityFactory.newWall())

//    fun closedDoor() = GameBlock.create().also {
//        it.terrain = ClosedDoor()
//    }
//
//    fun openDoor() = GameBlock.create().also {
//        it.terrain = OpenDoor()
//    }
}