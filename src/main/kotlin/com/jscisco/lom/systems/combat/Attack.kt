package com.jscisco.lom.systems.combat

import com.jscisco.lom.attributes.types.Combatant
import com.jscisco.lom.extensions.GameEntity

data class Attack(val attacker: GameEntity<Combatant>, val defender: GameEntity<Combatant>, val damages: List<Damage>)