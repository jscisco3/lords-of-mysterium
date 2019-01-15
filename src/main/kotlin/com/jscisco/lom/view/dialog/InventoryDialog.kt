package com.jscisco.lom.view.dialog

import com.jscisco.lom.attributes.types.inventory
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.view.dialog.fragment.ItemListFragment
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Container
import org.hexworks.zircon.api.graphics.BoxType

class InventoryDialog(gameContext: GameContext) : Dialog(screen = gameContext.screen) {

    override val container: Container = Components.panel()
            .withTitle("Inventory")
            .withSize(40, 20)
            .withBoxType(BoxType.SINGLE)
            .wrapWithBox()
            .build().also {
                it.addFragment(ItemListFragment(gameContext.player.inventory, 20))
            }

}