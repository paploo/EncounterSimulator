package net.paploo.encountersimulator.sim

import scala.language.higherKinds

case class PartyMember(id: Int, creature: Creature)

object Party {
  def apply(creatures: Seq[Creature]): Party = {
    val partyMembers = for {
      id <- 0 until creatures.length
      creature = creatures(id)
    } yield PartyMember(id, creature)

    new PartySeq(partyMembers)
  }

  def unapply(party: Party): Option[Seq[Creature]] = Some(party.creatures)

  def empty: Party = PartySeq(Nil)
}

trait Party {
  def members: Seq[PartyMember]
  def creatures: Seq[Creature]

  def alive: Party
  def dead: Party

  def isAlive: Boolean
  def isDead: Boolean

  def applyDamage(damageMap: Map[PartyMember, Int]): Party

  def apply(id: Int): PartyMember
}

abstract class AbstractParty extends Party {
  override val creatures: Seq[Creature] = members.map(_.creature)

  def isAlive: Boolean = creatures.exists(_.isAlive)
  def isDead: Boolean = !isAlive

  override def apply(id: Int): PartyMember = members.find(_.id == id).get
}

case class PartySeq(members: Seq[PartyMember]) extends AbstractParty {
  override lazy val alive: Party = PartySeq(members.filter(_.creature.isAlive))
  override lazy val dead: Party = PartySeq(members.filter(_.creature.isDead))

  def applyDamage(damageMap: Map[PartyMember, Int]): Party = {
    val newMembers = members.map { member =>
      val damage = damageMap.getOrElse(member, 0)
      val updatedCreature = member.creature.applyDamage(damage)
      member.copy(creature = updatedCreature)
    }
    PartySeq(newMembers)
  }
}