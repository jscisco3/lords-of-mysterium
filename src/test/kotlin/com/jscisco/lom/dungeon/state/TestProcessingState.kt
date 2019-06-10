//package com.jscisco.lom.dungeon.state
//
//import com.jscisco.lom.actor.Monster
//import com.jscisco.lom.data.TestData.newTestDungeon
//import com.jscisco.lom.dungeon.Dungeon
//import org.assertj.core.api.Assertions
//import org.junit.jupiter.api.Test
//
//class TestProcessingState {
//
//    private val testDungeon: Dungeon = newTestDungeon()
//
//    @Test
//    fun `when we only have the player in the dungeon, the minimum cooldown should be calculated to be their initiative cooldown`() {
//        val processingState: ProcessingState = ProcessingState(testDungeon)
//        val playerInitiative = testDungeon.player.initiative
//        val initialInitiative: Int = 1000
//        playerInitiative.cooldown = initialInitiative
//        val minimumCooldown = processingState.calculateMinimumCooldown()
//        Assertions.assertThat(minimumCooldown).isEqualTo(initialInitiative)
//        Assertions.assertThat(testDungeon.actors.size).isEqualTo(1)
//    }
//
//    @Test
//    fun `when we have multiple actors, the minimum cooldown is the lesser of their current cooldowns`() {
//        val processingState: ProcessingState = ProcessingState(testDungeon)
//        for (i in 0 until 5) {
//            testDungeon.addActor(Monster().also {
//                it.initiative.cooldown = 100 + i
//            }, testDungeon.findEmptyLocationOnZLevel(0).get())
//        }
//        testDungeon.player.initiative.cooldown = 1000
//        val minimumCooldown: Int = processingState.calculateMinimumCooldown()
//        Assertions.assertThat(minimumCooldown).isEqualTo(100)
//    }
//
//    @Test
//    fun `when the we update the state, the initiative of the player should be reduced to zero`() {
//        val processingState: ProcessingState = ProcessingState(testDungeon)
//        val player = testDungeon.player
//        player.initiative.cooldown = 1000
//        processingState.update()
//        Assertions.assertThat(player.initiative.cooldown).isEqualTo(0)
//    }
//
//}