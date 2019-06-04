//package com.jscisco.lom.view.dialog
//
//import com.jscisco.lom.dungeon.Dungeon
//import com.jscisco.lom.view.fragment.ItemListFragment
//import org.hexworks.cobalt.datatypes.extensions.ifPresent
//import org.hexworks.cobalt.logging.api.Logger
//import org.hexworks.cobalt.logging.api.LoggerFactory
//import org.hexworks.zircon.api.Components
//import org.hexworks.zircon.api.component.ComponentAlignment
//import org.hexworks.zircon.api.component.Container
//import org.hexworks.zircon.api.extensions.handleComponentEvents
//import org.hexworks.zircon.api.graphics.BoxType
//import org.hexworks.zircon.api.screen.Screen
//import org.hexworks.zircon.api.uievent.ComponentEventType
//import org.hexworks.zircon.api.uievent.Processed
//
//class InventoryDialog(val dungeon: Dungeon, val player: Player, screen: Screen) : Dialog(screen) {
//
//    val logger: Logger = LoggerFactory.getLogger(javaClass)
//
//    override val container: Container = Components.panel()
//            .withTitle("Inventory")
//            .withSize(40, 20)
//            .withBoxType(BoxType.SINGLE)
//            .wrapWithBox()
//            .build().also { inventoryPanel ->
//                val inventory = player.inventory
//                val itemListFragment = ItemListFragment(inventory.items, 20)
//                inventoryPanel.addFragment(itemListFragment)
//
//                val drop = Components.button()
//                        .withText("Drop")
//                        .withAlignmentWithin(inventoryPanel, ComponentAlignment.BOTTOM_LEFT)
//                        .build().apply {
//                            handleComponentEvents(ComponentEventType.ACTIVATED) {
//                                itemListFragment.fetchSelectedItem().ifPresent { item ->
//                                    itemListFragment.removeSelectedItem()
//                                    DropItemCommand(dungeon, player, item).invoke()
//                                }
//                                Processed
//                            }
//                        }
////                val equip = Components.button()
////                        .withText("Equip")
////                        .withAlignmentAround(drop, ComponentAlignment.RIGHT_CENTER)
////                        .build().apply {
////                            onMouseEvent(MouseEventType.MOUSE_RELEASED) { _, _ ->
////                                itemListFragment.fetchSelectedItem().map { item ->
////                                    player.executeCommand(EquipItemCommand(gameContext,
////                                            player, item, player.equipment.getSlotsByType(item.equippable.equipmentType)[0]))
////                                    itemListFragment.removeSelectedItem()
////                                }
////                                Processed
////                            }
////                        }
//                inventoryPanel.addComponent(drop)
//            }
//}
////                inventoryPanel.addComponent(equip)