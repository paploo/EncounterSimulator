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

  def apply(id: Int): Creature
}

abstract class AbstractParty extends Party {
  override val creatures: Seq[Creature] = members.map(_.creature)

  override def apply(id: Int): Creature = members.find(_.id == id).get.creature
}

case class PartySeq(members: Seq[PartyMember]) extends AbstractParty {
  override val alive: Party = PartySeq(members.filter(_.creature.isAlive))
  override val dead: Party = PartySeq(members.filter(_.creature.isDead))
}