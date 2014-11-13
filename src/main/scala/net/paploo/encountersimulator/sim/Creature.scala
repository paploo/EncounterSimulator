package net.paploo.encountersimulator.sim

trait Creature {
  def name: String
  def template: CreatureTemplate
  def damage: Int
  def turnOfDeath: Option[Int]

  def isAlive: Boolean = damage < hitPoints
  def isDead: Boolean = !isAlive

  def hitPoints: Int = template.hitPoints
  def damageRate: Int = template.damageRate

  def applyDamage(dmg: Int): Creature
}

case class SimpleCreature(name: String,
                          template: CreatureTemplate,
                          damage: Int = 0,
                          turnOfDeath: Option[Int] = None) extends Creature {
  def applyDamage(dmg: Int): Creature = this.copy(damage = damage+dmg)
}
