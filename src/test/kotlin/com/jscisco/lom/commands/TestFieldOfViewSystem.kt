package com.jscisco.lom.commands

import com.jscisco.lom.attributes.types.fov
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.configuration.GameConfiguration
import com.jscisco.lom.dungeon.DungeonBuilder
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.dungeon.strategies.EmptyDungeonStrategy
import org.assertj.core.api.Assertions
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.Screens
import org.hexworks.zircon.api.SwingApplications
import org.hexworks.zircon.api.data.impl.Position3D
import org.hexworks.zircon.api.data.impl.Size3D
import org.hexworks.zircon.api.input.KeyStroke
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestFieldOfViewSystem {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @Test
    fun calculateFOV() {
        var player = EntityFactory.newPlayer()

        val dungeonSize: Size3D = Size3D.create(100, 100, 1)
        val dungeonBuilder: DungeonBuilder = DungeonBuilder(
                dungeonSize = dungeonSize,
                strategy = EmptyDungeonStrategy(dungeonSize),
                player = player
        )
        val dungeon = dungeonBuilder.build(dungeonSize, dungeonSize)
        dungeon.moveEntity(player, Position3D.create(1, 1, 0))

        val gameContext = GameContext(
                dungeon = dungeon,
                screen = Screens.createScreenFor(SwingApplications.buildApplication(GameConfiguration.buildAppConfig()).tileGrid),
                input = KeyStroke(),
                player = player
        )

        logger.info(player.fov.fov.size.toString())
        player.executeCommand(FieldOfView(context = gameContext, source = player))
        Assertions.assertThat(player.fov.fov[0][0]).isGreaterThan(0.0)
    }

}