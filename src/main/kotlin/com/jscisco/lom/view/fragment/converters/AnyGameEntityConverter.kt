package com.jscisco.lom.view.fragment.converters

import com.jscisco.lom.extensions.AnyGameEntity
import com.jscisco.lom.extensions.entityName
import org.hexworks.cobalt.databinding.api.converter.Converter

class AnyGameEntityConverter : Converter<AnyGameEntity, String> {
    override fun convert(source: AnyGameEntity): String {
        return source.entityName
    }
}