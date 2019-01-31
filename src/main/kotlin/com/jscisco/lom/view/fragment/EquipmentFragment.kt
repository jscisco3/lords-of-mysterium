package com.jscisco.lom.view.fragment

import com.jscisco.lom.attributes.EquipmentAttribute
import com.jscisco.lom.attributes.types.ItemHolder
import com.jscisco.lom.attributes.types.inventory
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.entityName
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.kotlin.onMouseReleased

/**
 * entity: The entity who owns this equipmentAttribute
 */
class EquipmentFragment(private val entity: GameEntity<ItemHolder>, private val equipmentAttribute: EquipmentAttribute, width: Int) : Fragment {

    val height = equipmentAttribute.equipmentSlots.size

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

                equipmentAttribute.equipmentSlots.forEachIndexed { index, eq ->
                    equipmentTypePanel.addComponent(Components.button()
                            .withText(eq.type.name)
                            .withPosition(0, index)
                            .build().apply {
                                onMouseReleased {
                                    equipmentAttribute.unequip(entity.inventory, eq)
                                }
                            }
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