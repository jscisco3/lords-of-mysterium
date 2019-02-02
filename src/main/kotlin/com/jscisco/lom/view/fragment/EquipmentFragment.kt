package com.jscisco.lom.view.fragment

import com.jscisco.lom.attributes.EquipmentAttribute
import com.jscisco.lom.attributes.types.ItemHolder
import com.jscisco.lom.attributes.types.equippable
import com.jscisco.lom.attributes.types.inventory
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.view.fragment.converters.AnyGameEntityConverter
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
                    val unequipButton = Components.button()
                            .withText("U")
                            .withPosition(0, index)
                            .build().apply {
                                onMouseReleased {
                                    equipmentAttribute.unequip(entity.inventory, eq)
                                }
                            }


                    equipmentTypePanel.addComponent(unequipButton)
                    equipmentTypePanel.addComponent(Components.button()
                            .withText(eq.type.name)
                            .withPosition(unequipButton.width + 1, index)
                            .build().also {
                                it.onMouseReleased {
                                    this.addFragment(ItemListFragment(
                                            entity.inventory.items.filter { item ->
                                                item.equippable.equipmentType == eq.type
                                            },
                                            20
                                    ).apply {
                                        root.moveRightBy(30)
                                    })
                                }
                            }
                    )


                    equipmentNamePanel.addComponent(Components.label()
                            .withSize(width, 1)
                            .withPosition(0, index)
                            .build().also {
                                it.textProperty.bind(eq.equippedItemProperty, AnyGameEntityConverter())
                            }
                    )
                }
                addComponent(equipmentTypePanel)
                addComponent(equipmentNamePanel)
            }

}