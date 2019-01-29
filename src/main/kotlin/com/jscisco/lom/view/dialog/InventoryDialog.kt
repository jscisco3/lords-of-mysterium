package com.jscisco.lom.view.dialog

import com.jscisco.lom.attributes.types.inventory
import com.jscisco.lom.commands.DropItemCommand
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.extensions.position
import com.jscisco.lom.view.dialog.fragment.ItemListFragment
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.Container
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.kotlin.onMouseReleased

class InventoryDialog(gameContext: GameContext) : Dialog(screen = gameContext.screen) {

    override val container: Container = Components.panel()
            .withTitle("Inventory")
            .withSize(40, 20)
            .withBoxType(BoxType.SINGLE)
            .wrapWithBox()
            .build().also { inventoryPanel ->
                val player = gameContext.player
                val inventory = player.inventory
                val itemListFragment = ItemListFragment(inventory, 20)
                inventoryPanel.addFragment(itemListFragment)

                val drop = Components.button()
                        .withText("Drop")
                        .withAlignmentWithin(inventoryPanel, ComponentAlignment.BOTTOM_LEFT)
                        .build().apply {
                            onMouseReleased {
                                itemListFragment.fetchSelectedItem().map { item ->
                                    itemListFragment.removeSelectedItem()
                                    player.executeCommand(DropItemCommand(gameContext,
                                            player,
                                            item,
                                            player.position))
                                }
                            }
                        }
                inventoryPanel.addComponent(drop)
            }

}