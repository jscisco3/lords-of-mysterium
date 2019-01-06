package com.jscisco.lom.commands

import com.jscisco.lom.attributes.Inventory
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.configuration.GameConfiguration
import com.jscisco.lom.dungeon.DungeonBuilder
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.dungeon.strategies.EmptyDungeonStrategy
import com.jscisco.lom.extensions.attribute
import org.assertj.core.api.Assertions
import org.hexworks.zircon.api.Screens
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.input.KeyStroke
import org.junit.Test

class TestPickItemUp {

    @Test
    fun testPickItemUp() {
        var player = EntityFactory.newPlayer()
        var sword = EntityFactory.newSword()

        val swordPosition = Position3D.create(10, 10, 0)

        val dungeonSize: Size3D = Size3D.create(100, 100, 1)
        val dungeonBuilder: DungeonBuilder = DungeonBuilder(
                dungeonSize = dungeonSize,
                hero = player,
                strategy = EmptyDungeonStrategy(dungeonSize)
        )
        val dungeon = dungeonBuilder.build(dungeonSize, dungeonSize)

        val gameContext = GameContext(
                dungeon = dungeon,
                screen = Screens.createScreenFor(SwingApplications.buildApplication(GameConfiguration.buildAppConfig()).tileGrid),
                input = KeyStroke(),
                player = player
        )

        dungeon.addEntity(sword, swordPosition)
        dungeon.addEntity(player, swordPosition)

        player.executeCommand(PickItemUp(gameContext, player, swordPosition))

        Assertions.assertThat(player.attribute<Inventory>().items.size).isEqualTo(1)

    }
}