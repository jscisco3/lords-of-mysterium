package com.jscisco.lom.view.fragment

import com.jscisco.lom.attributes.Equipment
import com.jscisco.lom.extensions.entityName
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.Fragment

class EquipmentFragment(private val equipment: Equipment, width: Int) : Fragment {

    val height = equipment.equipment.size

    override val root: Component = Components.panel()
            .withSize(width, height)
            .build().apply {
                val equipmentTypePanel = Components.panel()
                        .withSize(width / 2, height)
                        .withAlignmentWithin(this, ComponentAlignment.TOP_LEFT)
                        .build()
                val equipmentNamePanel = Components.panel()
                        .withSize(width / 2, height)
                        .withAlignmentWithin(this, ComponentAlignment.TOP_RIGHT)
                        .build()

                equipment.equipment.forEachIndexed { index, eq ->
                    equipmentTypePanel.addComponent(Components.label()
                            .withText(eq.type.name)
                            .withPosition(0, index)
                            .build()
                    )
                    equipmentNamePanel.addComponent(Components.label()
                            .withText(eq.equippedItem.entityName)
                            .withPosition(0, index)
                            .build()
                    )
                }
                addComponent(equipmentTypePanel)
                addComponent(equipmentNamePanel)
            }
}