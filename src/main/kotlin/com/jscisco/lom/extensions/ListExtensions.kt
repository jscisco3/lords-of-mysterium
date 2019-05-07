package com.jscisco.lom.extensions

fun <T : Any> List<T>.whenNotEmpty(fn: (List<T>) -> Unit) {
    if (this.isNotEmpty()) {
        fn(this)
    }
}