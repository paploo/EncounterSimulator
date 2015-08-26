package net.paploo.encountersimulator.sim


import net.paploo.encountersimulator.sim.Creature.CreatureId

import scala.language.higherKinds

object Party {
  def apply(creatures: Seq[Creature]): Party = PartySeq(None, creatures)

  def apply(name: Option[String], creatures: Seq[Creature]): Party = PartySeq(name, creatures)
}

trait Party extends PartialFunction[CreatureId, Creature] with Livable {
  def name: Option[String]

  def creatures: Seq[Creature] = creatureMap.values.toSeq
  def creatureMap: Map[CreatureId, Creature] = creatures.map(creature => creature.id -> creature).toMap

  def apply(creatureId: CreatureId): Creature = creatureMap(creatureId)
  def isDefinedAt(creatureId: CreatureId): Boolean = creatureMap.isDefinedAt(creatureId)

  val aliveCreatures: Seq[Creature] = creatures.filter(_.isAlive)
  val deadCreatures: Seq[Creature] = creatures.filter(_.isDead)

  def isAlive: Boolean = !isDead
  def isDead: Boolean = aliveCreatures.isEmpty

  def updated(creature: Creature): Party
}

case class PartyMap(name: Option[String],
                    override val creatureMap: Map[CreatureId, Creature]) extends Party {
  override val creatures = super.creatures

  override def updated(creature: Creature): Party = {
    this.copy(creatureMap = creatureMap.updated(creature.id, creature))
  }
}

case class PartySeq(name: Option[String],
                    override val creatures: Seq[Creature]) extends Party {
  override val creatureMap = super.creatureMap

  override def updated(creature: Creature): Party = {
    val index: Int = creatures.indexWhere(_.id == creature.id)
    this.copy(creatures = creatures.updated(index, creature))
  }
}