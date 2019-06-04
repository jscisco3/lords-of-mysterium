//package com.jscisco.lom.view.dialog
//
//import com.jscisco.lom.actor.Player
//import org.hexworks.zircon.api.Components
//import org.hexworks.zircon.api.component.Container
//import org.hexworks.zircon.api.graphics.BoxType
//import org.hexworks.zircon.api.screen.Screen
//
//class EquipmentDialog(val player: Player, screen: Screen) : Dialog(screen) {
//
//    override val container: Container = Components.panel()
//            .withTitle("Equipment")
//            .withSize(40, 20)
//            .withBoxType(BoxType.SINGLE)
//            .wrapWithBox()
//            .build()
////            .build().also { equipmentPanel ->
////                val equipmentFragment = EquipmentFragment(context, context.player, equipment, 30)
////                equipmentPanel.addFragment(equipmentFragment)
////            }
//}