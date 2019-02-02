package com.jscisco.lom.systems

import com.jscisco.lom.attributes.EntityTile
import com.jscisco.lom.attributes.flags.BlockOccupier
import com.jscisco.lom.attributes.flags.Openable
import com.jscisco.lom.attributes.flags.VisionBlocker
import com.jscisco.lom.attributes.types.Door
import com.jscisco.lom.attributes.types.Player
import com.jscisco.lom.builders.GameTileBuilder
import com.jscisco.lom.commands.ToggleDoorCommand
import com.jscisco.lom.extensions.hasAttribute
import com.jscisco.lom.extensions.newGameEntityOfType
import com.jscisco.lom.extensions.tile
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class TestOpenDoorSystem {

    private val door = DOOR
    private val doorOpener = DOOR_OPENER

    @Test
    fun `open a door`() {
        doorOpener.executeCommand(ToggleDoorCommand(
                context = mockk(),
                source = doorOpener,
                target = door
        ))

        Assertions.assertThat(door.hasAttribute<VisionBlocker>()).isFalse()
        Assertions.assertThat(door.hasAttribute<BlockOccupier>()).isFalse()
        Assertions.assertThat(door.tile == GameTileBuilder.openDoor())
    }

    companion object {

        val DOOR_OPENER = newGameEntityOfType(Player) {
            facets(
                    OpenDoorSystem
            )
        }

        val DOOR = newGameEntityOfType(Door) {
            attributes(
                    VisionBlocker,
                    BlockOccupier,
                    EntityTile(GameTileBuilder.closedDoor()),
                    Openable
            )
            facets(
                    OpenDoorSystem
            )
        }
    }

}