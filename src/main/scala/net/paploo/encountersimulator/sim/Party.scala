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

trait PartyLike[+Repr] {
  protected[this] type Self = Repr
  protected[this] def build(m: Seq[PartyMember]): Repr

  def members: Seq[PartyMember]
  def creatures: Seq[Creature]

  def alive: Repr
  def dead: Repr

  def isAlive: Boolean
  def isDead: Boolean

  def applyDamage(damageMap: Map[PartyMember, Int]): Party

  def apply(id: Int): PartyMember
}

trait Party extends PartyLike[Party] {
  override val creatures: Seq[Creature] = members.map(_.creature)

  override def alive: Self = build(members.filter(_.creature.isAlive))
  override def dead: Self = build(members.filter(_.creature.isDead))

  override def isAlive: Boolean = creatures.exists(_.isAlive)
  override def isDead: Boolean = !isAlive

  override def apply(id: Int): PartyMember = members.find(_.id == id).get
}

case class PartySeq(members: Seq[PartyMember]) extends Party with PartyLike[PartySeq] {

  override protected[this] def build(m: Seq[PartyMember]): PartySeq = PartySeq(m)

  override lazy val alive: PartySeq = super.alive
  override lazy val dead: PartySeq = super.dead

  override def applyDamage(damageMap: Map[PartyMember, Int]): PartySeq = {
    val newMembers = members.map { member =>
      val damage = damageMap.getOrElse(member, 0)
      val updatedCreature = member.creature.applyDamage(damage)
      member.copy(creature = updatedCreature)
    }
    build(newMembers)
  }
}