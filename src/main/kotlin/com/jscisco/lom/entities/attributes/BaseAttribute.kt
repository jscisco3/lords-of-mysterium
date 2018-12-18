package com.jscisco.lom.entities.attributes;

abstract class BaseAttribute : Attribute {

    override val name: String
        get() = this::class.simpleName!!

}
