package net.paploo.encountersimulator.sim

import java.util.UUID

import scala.language.implicitConversions

import net.paploo.encountersimulator.sim.Creature.CreatureId

object Creature {
  type CreatureId = UUID

  def apply(template: CreatureTemplate): Creature = ImmutableCreature(template = template)

  def apply(name: Option[String], template: CreatureTemplate): Creature = ImmutableCreature(name = name, template = template)

  /**
   * This is a non-pure method, but that isn't a desired property.
   * @return
   */
  def createId: CreatureId = UUID.randomUUID

  object Conversions {
    implicit def creatureToCreatureId(creature: Creature): CreatureId = creature.id
  }
}

trait Creature extends Livable {
  def id: CreatureId = Creature.createId
  def name: Option[String]
  def template: CreatureTemplate
  def damage: Int // The total damage.

  def life = template.hitPoints - damage

  def isAlive: Boolean = life > 0
  def isDead: Boolean = !isAlive

  def applyDamage(dmg: Int): Creature
}

case class ImmutableCreature(override val id: CreatureId = Creature.createId,
                          name: Option[String] = None,
                          template: CreatureTemplate,
                          damage: Int = 0) extends Creature {
  override def applyDamage(dmg: Int): Creature = this.copy(damage = damage+dmg)
}