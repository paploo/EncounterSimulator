package net.paploo.encountersimulator.sim

import java.util.UUID

import scala.language.implicitConversions

import net.paploo.encountersimulator.sim.Creature.CreatureId

object Creature {
  type CreatureId = UUID

  def apply(template: CreatureTemplate): Creature = SimpleCreature(template = template)

  def apply(name: Option[String], template: CreatureTemplate): Creature = SimpleCreature(name = name, template = template)

  def createId: CreatureId = UUID.randomUUID

  object Conversions {
    implicit def creatureToCreatureId(creature: Creature): CreatureId = creature.id
  }
}

trait Creature extends Livable {
  def id: CreatureId = Creature.createId
  def name: Option[String]
  def template: CreatureTemplate
  def damage: Int

  def remainingHitPoints = template.hitPoints - damage

  def isAlive: Boolean = remainingHitPoints > 0
  def isDead: Boolean = !isAlive

  def applyDamage(dmg: Int): Creature
}

case class SimpleCreature(override val id: CreatureId = Creature.createId,
                          name: Option[String] = None,
                          template: CreatureTemplate,
                          damage: Int = 0) extends Creature {
  override def applyDamage(dmg: Int): Creature = this.copy(damage = damage+dmg)
}