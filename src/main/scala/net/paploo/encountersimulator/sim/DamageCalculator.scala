package net.paploo.encountersimulator.sim

/**
 * We isolate damage calculation so that different rule sets can be implemented.
 *
 * Note that we attempt to isolate use of random numbers to here, making the
 * non-determinism be limited.
 */
trait DamageCalculator {
  def computeDamage(attacker: Creature, defender: Creature): Option[Int]
}

class DNDDamageCalculator extends DamageCalculator {
  def computeDamage(attacker: Creature, defender: Creature): Option[Int] = {
    val attackRoll = attacker.template.attack.value
    val defenseRoll = defender.template.defense.value
    if(attackRoll >= defenseRoll) Some(attacker.template.damage.value) else None
  }
}
