package com.jscisco.lom.view.fragment

import com.jscisco.lom.attributes.EquipmentAttribute
import com.jscisco.lom.attributes.types.ItemHolder
import com.jscisco.lom.attributes.types.inventory
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.expression.concatWithConvert
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
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
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

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

//                    val eqProp = createPropertyFrom(eq.equippedItem.entityName)

                    equipmentNamePanel.addComponent(Components.label()
                            .withSize(width, 1)
                            .withPosition(0, index)
                            .build().also {
                                it.textProperty.bind(
                                        createPropertyFrom("")
                                                .concatWithConvert(eq.equippedItemProperty)
                                )
                                logger.info("In build.also: " + eq.equippedItemProperty.value)
                            }
                    )
                }
                addComponent(equipmentTypePanel)
                addComponent(equipmentNamePanel)
            }
}