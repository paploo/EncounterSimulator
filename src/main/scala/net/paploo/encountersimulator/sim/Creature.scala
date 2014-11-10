package net.paploo.encountersimulator.sim

trait Creature {
  def name: String
  def template: CreatureTemplate
  def damage: Int
  def turnOfDeath: Option[Int]

  def isAlive: Boolean = damage > 0
  def isDead: Boolean = !isAlive
}

case class SimpleCreature(name: String,
                          template: CreatureTemplate,
                          damage: Int = 0,
                          turnOfDeath: Option[Int] = None) extends Creature
