package com.jscisco.lom.view.dialog

import com.jscisco.lom.attributes.types.equipment
import com.jscisco.lom.dungeon.GameContext
import com.jscisco.lom.view.fragment.EquipmentFragment
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Container
import org.hexworks.zircon.api.graphics.BoxType

class EquipmentDialog(context: GameContext) : Dialog(screen = context.screen) {

    val equipment = context.player.equipment

    override val container: Container = Components.panel()
            .withTitle("EquipmentAttribute")
            .withSize(40, equipment.equipmentSlots.size + 2)
            .withBoxType(BoxType.SINGLE)
            .wrapWithBox()
            .build().also { equipmentPanel ->
                val equipmentFragment = EquipmentFragment(context, context.player, equipment, 30)
                equipmentPanel.addFragment(equipmentFragment)
            }
}