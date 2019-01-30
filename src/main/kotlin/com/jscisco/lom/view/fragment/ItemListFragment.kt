package com.jscisco.lom.view.fragment

import com.jscisco.lom.attributes.Inventory
import com.jscisco.lom.attributes.types.Item
import com.jscisco.lom.builders.EntityFactory
import com.jscisco.lom.extensions.GameEntity
import org.hexworks.cobalt.databinding.api.createPropertyFrom
import org.hexworks.cobalt.databinding.api.property.Property
import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.cobalt.datatypes.extensions.map
import org.hexworks.cobalt.datatypes.factory.IdentifierFactory
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.component.RadioButtonGroup
import org.hexworks.zircon.api.kotlin.onSelection

class ItemListFragment(inventory: Inventory, width: Int) : Fragment {

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
            .withSize(width, inventory.items.size + 2)
            .build().apply {
                rgb = Components.radioButtonGroup()
                        .withSize(width, inventory.items.size)
                        .withPosition(0, 2)
                        .build()
                inventory.items.forEach { item ->
                    rgb.addOption(item.id.toString(), item.name)
                }
                rgb.onSelection {
                    inventory.findItemById(IdentifierFactory.fromString(it.key)).map { item ->
                        selectedItem.value = item
                    }
                }
                addComponent(Components.header()
                        .withText("Items"))
                addComponent(rgb)
            }


//    companion object {
//        private val NO_ITEM = newGameEntityOfType(NoItem) {
//            attributes()
//        }
//    }

}