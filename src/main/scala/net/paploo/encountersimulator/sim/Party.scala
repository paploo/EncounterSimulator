package net.paploo.encountersimulator.sim

import scala.language.higherKinds

object Party {
  def apply(creatures: Seq[Creature]): Party = PartySeq(creatures)
  def unapply(party: Party): Option[Seq[Creature]] = Some(party.creatures)
}

trait Party {
  def creatures: Seq[Creature]
  def aliveCreatures: Seq[Creature]
  def deadCreatures: Seq[Creature]

  def areAnyAlive: Boolean
  def areAnyDead: Boolean
  def areAllAlive: Boolean
  def areAllDead: Boolean

  def aliveIndices: Seq[Int]
  def deadIndices: Seq[Int]
}

abstract class AbstractParty extends Party {
  override val aliveCreatures = creatures.filter(_.isAlive)
  override val deadCreatures = creatures.filter(_.isDead)

  override val areAnyAlive: Boolean = aliveCreatures.nonEmpty
  override val areAnyDead: Boolean = deadCreatures.nonEmpty
  override val areAllAlive: Boolean = deadCreatures.isEmpty
  override val areAllDead: Boolean = aliveCreatures.isEmpty

  override def aliveIndices: Seq[Int] = (0 until creatures.length).filter(creatures(_).isAlive)
  override def deadIndices: Seq[Int] = (0 until creatures.length).filter(creatures(_).isDead)
}

case class PartySeq(creatures: Seq[Creature]) extends AbstractParty