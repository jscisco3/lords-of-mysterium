package com.jscisco.lom.commands

import com.jscisco.lom.data.TestData
import com.jscisco.lom.dungeon.Dungeon
import com.jscisco.lom.terrain.ClosedDoor
import com.jscisco.lom.terrain.OpenDoor
import org.assertj.core.api.Assertions
import org.hexworks.cobalt.datatypes.extensions.ifPresent
import org.hexworks.zircon.api.data.impl.Position3D
import org.junit.jupiter.api.Test

class TestOpenDoorCommand {

    private val testDungeon: Dungeon = TestData.newTestDungeon().apply {
        moveEntity(this.player, Position3D.create(10, 10, 0))
    }

    @Test
    fun `opening a door should replace the closed door with an open door`() {
        val doorPosition = Position3D.create(4, 4, 0)
        testDungeon.fetchBlockAt(doorPosition).ifPresent {
            it.terrain = ClosedDoor()
        }
        val response = OpenDoorCommand(testDungeon, testDungeon.player, doorPosition).invoke()
        Assertions.assertThat(response).isEqualTo(Consumed)
        Assertions.assertThat(testDungeon.fetchBlockAt(doorPosition).get().terrain).isInstanceOf(OpenDoor().javaClass)
    }

    @Test
    fun `trying to invoke this command on a tile that isn't a closed door does nothing`() {
        val response = OpenDoorCommand(testDungeon, testDungeon.player, Position3D.unknown()).invoke()
        Assertions.assertThat(response).isEqualTo(Pass)
    }

}