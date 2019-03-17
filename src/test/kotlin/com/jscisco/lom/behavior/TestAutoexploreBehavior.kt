package com.jscisco.lom.behavior

import com.jscisco.lom.attributes.AutoexploreAttribute
import com.jscisco.lom.behaviors.AutoexploreBehavior
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.data.TestData
import com.jscisco.lom.extensions.position
import org.hexworks.zircon.api.data.impl.Position3D
import org.junit.jupiter.api.Test
import squidpony.squidai.DijkstraMap
import squidpony.squidmath.Coord

class TestAutoexploreBehavior {

    private val dungeon = TestData.newTestDungeon()
    private val player = dungeon.player

    @Test
    fun `when I autoexplore, I should be able to get a list of goals`() {
        val autoexploreAttribute = AutoexploreAttribute()
        val playerPosition = Position3D.create(10, 10, 0)
        dungeon.moveEntity(player, playerPosition)

        dungeon.addEntity(EntityFactory.newClosedDoor(), playerPosition.withRelativeX(1))
        dungeon.addEntity(EntityFactory.newClosedDoor(), playerPosition.withRelativeX(1).withRelativeY(1))
        dungeon.addEntity(EntityFactory.newClosedDoor(), playerPosition.withRelativeX(1).withRelativeY(-1))
        dungeon.addEntity(EntityFactory.newClosedDoor(), playerPosition.withRelativeX(-1))
        dungeon.addEntity(EntityFactory.newClosedDoor(), playerPosition.withRelativeX(-1).withRelativeY(1))
        dungeon.addEntity(EntityFactory.newClosedDoor(), playerPosition.withRelativeX(-1).withRelativeY(-1))

        dungeon.addEntity(EntityFactory.newClosedDoor(), playerPosition.withRelativeY(-1))
        dungeon.addEntity(EntityFactory.newClosedDoor(), playerPosition.withRelativeY(1))

        val autoexploreBehavior = player.findBehavior(AutoexploreBehavior::class).get()

        val costMap = autoexploreBehavior.calculateAutoexploreMap(player, dungeon)

        autoexploreAttribute.dijkstraMap.initialize(costMap)
        autoexploreAttribute.dijkstraMap.measurement = DijkstraMap.Measurement.CHEBYSHEV

        val path = autoexploreAttribute.dijkstraMap.findPath(5,
                mutableListOf<Coord>(),
                mutableListOf<Coord>(),
                Coord.get(player.position.x, player.position.y),
                Coord.get(14, 10))
        path.forEach { println("Coordinate: %s".format(it)) }

        for (x in 0 until 15) {
            var rowStr = ""
            for (y in 0 until 15) {
                rowStr += autoexploreAttribute.dijkstraMap.gradientMap[x][y]
                rowStr += " "
            }
            println("%s: %s".format(x, rowStr))
        }
    }


}