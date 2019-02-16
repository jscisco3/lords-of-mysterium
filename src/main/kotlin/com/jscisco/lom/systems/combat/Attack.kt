package com.jscisco.lom.systems.combat

import com.jscisco.lom.attributes.types.*
import com.jscisco.lom.extensions.GameEntity

data class Attack(val attacker: GameEntity<Combatant>, val defender: GameEntity<Combatant>,
                  var attackPower: Int = attacker.attackPower, var toHit: Int = attacker.toHit,
                  var ac: Int = defender.ac, var ev: Int = defender.ev)