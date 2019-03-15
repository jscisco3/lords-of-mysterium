package com.jscisco.lom.view.fragment

import com.jscisco.lom.attributes.types.Item
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.extensions.GameEntity
import com.jscisco.lom.extensions.entityName
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.cobalt.logging.api.Logger
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.component.RadioButtonGroup
import org.hexworks.zircon.api.extensions.onSelection

class ItemListFragment(items: List<GameEntity<Item>>, width: Int) : Fragment {

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    val selectedItem: Property<GameEntity<Item>> = createPropertyFrom(EntityFactory.noItem())

    private val rgb: RadioButtonGroup

    fun fetchSelectedItem(): Maybe<GameEntity<Item>> {
        return Maybe.ofNullable(if (selectedItem.value == EntityFactory.noItem()) null else selectedItem.value)
    }

    fun removeSelectedItem() {
        rgb.fetchSelectedOption().map {
            rgb.removeOption(it)
        }
    }

    override val root = Components.panel()
            .withSize(width, items.size + 2)
            .build().apply {
                rgb = Components.radioButtonGroup()
                        .withSize(width, items.size)
                        .withPosition(0, 2)
                        .build()
                items.forEach { item ->
                    rgb.addOption(item.id.toString(), item.entityName)
                }
                rgb.onSelection {
                    selectedItem.value = items.filter { item ->
                        item.id.toString() == it.key
                    }.last()
                }
                addComponent(Components.header()
                        .withText("Items"))
                addComponent(rgb)
            }
}