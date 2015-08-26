package net.paploo.encountersimulator.sim

import scala.util.Random

trait EngagementStrategy {
  def selectDefender(attacker: Creature, opposingParties: Seq[Party]): Option[Creature]
}

class EngageFirstStrategy extends EngagementStrategy {
  override def selectDefender(attacker: Creature, opposingParties: Seq[Party]): Option[Creature] = for {
    party <- opposingParties.find(_.isAlive)
    creature <- party.creatures.find(_.isAlive)
  } yield creature
}

class EngageStrongest extends EngagementStrategy {
  def selectDefender(attacker: Creature, opposingParties: Seq[Party]): Option[Creature] = {
    //TODO: Make more efficient
    val defenders: Seq[Creature] = opposingParties.foldLeft(Seq.empty[Creature])(_ ++ _.creatures)
    if(defenders.isEmpty) None else Some(defenders.maxBy(_.template.difficultyScore))
  }
}

class EngageRandom extends EngagementStrategy {
  def selectDefender(attacker: Creature, opposingParties: Seq[Party]): Option[Creature] = {
    val defenders: Seq[Creature] = opposingParties.foldLeft(Seq.empty[Creature])(_ ++ _.creatures)
    if(defenders.isEmpty) None else Some(defenders(Random.nextInt(defenders.length)))
  }
}