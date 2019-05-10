package com.jscisco.lom.view.dialog

import com.jscisco.lom.actor.Player
import com.jscisco.lom.dungeon.Dungeon
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Container
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen

class InventoryDialog(val dungeon: Dungeon, val player: Player, screen: Screen) : Dialog(screen) {

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    override val container: Container = Components.panel()
            .withTitle("Inventory")
            .withSize(40, 20)
            .withBoxType(BoxType.SINGLE)
            .wrapWithBox()
            .build()
//            .build().also { inventoryPanel ->
//                val player = gameContext.player
//                val inventory = player.inventory
//                val itemListFragment = ItemListFragment(inventory.items, 20)
//                inventoryPanel.addFragment(itemListFragment)
//
//                val drop = Components.button()
//                        .withText("Drop")
//                        .withAlignmentWithin(inventoryPanel, ComponentAlignment.BOTTOM_LEFT)
//                        .build().apply {
//                            onMouseEvent(MouseEventType.MOUSE_RELEASED) { _, _ ->
//                                itemListFragment.fetchSelectedItem().map { item ->
//                                    itemListFragment.removeSelectedItem()
//                                    player.executeCommand(DropItemCommand(gameContext,
//                                            player,
//                                            item,
//                                            player.position))
//                                }
//                                Processed
//                            }
//                        }
//                val equip = Components.button()
//                        .withText("Equip")
//                        .withAlignmentAround(drop, ComponentAlignment.RIGHT_CENTER)
//                        .build().apply {
//                            onMouseEvent(MouseEventType.MOUSE_RELEASED) { _, _ ->
//                                itemListFragment.fetchSelectedItem().map { item ->
//                                    player.executeCommand(EquipItemCommand(gameContext,
//                                            player, item, player.equipment.getSlotsByType(item.equippable.equipmentType)[0]))
//                                    itemListFragment.removeSelectedItem()
//                                }
//                                Processed
//                            }
//                        }
//                inventoryPanel.addComponent(drop)
//                inventoryPanel.addComponent(equip)
}