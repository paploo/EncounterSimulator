package net.paploo.encountersimulator.sim

import scala.util.Random

object EngagementStrategy {

  def random: EngagementStrategy = new EngagementStrategyFunction(
    (attackers, defenders, attackerIndex) => {
      val aliveIndices = (0 until defenders.length).filter(defenders(_).isAlive)
      if (aliveIndices.isEmpty) None else Some(aliveIndices(Random.nextInt(aliveIndices.length)))
    }
  )

}

trait EngagementStrategy {
  def defenderIndex(attackers: Seq[Creature], defenders: Seq[Creature])(attackerIndex: Int): Option[Int]
}

class EngagementStrategyFunction(val func: (Seq[Creature], Seq[Creature], Int) => Option[Int]) extends EngagementStrategy {
  def defenderIndex(attackers: Seq[Creature], defenders: Seq[Creature])(attackerIndex: Int): Option[Int] =
    func(attackers, defenders, attackerIndex)
}