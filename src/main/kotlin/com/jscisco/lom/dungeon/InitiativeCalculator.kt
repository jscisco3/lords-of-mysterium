//package com.jscisco.lom.dungeon
//
//import com.jscisco.lom.attributes.InitiativeAttribute
//import com.jscisco.lom.commands.InitiativeCommand
//import com.jscisco.lom.extensions.position
//import com.jscisco.lom.extensions.whenHasAttribute
//import org.hexworks.zircon.api.screen.Screen
//
//object InitiativeCalculator {
//
//    fun calculateInitiativeDecrement(dungeon: Dungeon): Int {
//        var decrement = Int.MAX_VALUE
//
//        dungeon.fetchEntitiesOnZLevel(dungeon.player.position.z).forEach {
//            it.whenHasAttribute<InitiativeAttribute> { initiativeAttribute ->
//                if (initiativeAttribute.initiative <= decrement) {
//                    decrement = initiativeAttribute.initiative
//                }
//            }
//        }
//
//        return decrement
//    }
//
//    fun handleInitiative(dungeon: Dungeon, screen: Screen) {
//        val decrement = calculateInitiativeDecrement(dungeon)
//        dungeon.fetchEntitiesOnZLevel(dungeon.player.position.z).forEach {
//            it.executeCommand(InitiativeCommand(
//                    GameContext(
//                            dungeon = dungeon,
//                            screen = screen,
//                            player = dungeon.player
//                    ),
//                    it,
//                    decrement
//            ))
//        }
//    }
//
//}