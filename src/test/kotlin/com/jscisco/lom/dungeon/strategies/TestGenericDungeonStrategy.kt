package com.jscisco.lom.dungeon.strategies

import org.hexworks.zircon.api.data.impl.Size3D
import org.junit.jupiter.api.Test

class TestGenericDungeonStrategy {

    @Test
    fun `I should be able to generate a dungeon`() {
        val strategy = GenericDungeonStrategy(Size3D.create(80, 50, 4))
        strategy.generateDungeon()
    }

}