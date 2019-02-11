package com.jscisco.lom.systems.combat

data class Damage(val magnitude: Int, val damageType: DamageType)

enum class DamageType {
    PHYSICAL,
    FIRE,
    ICE,
    ELECTRIC
}