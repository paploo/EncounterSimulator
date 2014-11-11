package net.paploo.encountersimulator.sim

import scala.util.Random

object EngagementStrategy {

  def random: EngagementStrategy = new EngagementStrategyFunction(
    (attackers, defenders, attackerIndex) => {
      val aliveIndices = defenders.aliveIndices
      if (aliveIndices.isEmpty) None else Some(aliveIndices(Random.nextInt(aliveIndices.length)))
    }
  )

}

trait EngagementStrategy {
  def defenderIndex(attackers: Party, defenders: Party)(attackerIndex: Int): Option[Int]
}

class EngagementStrategyFunction(val func: (Party, Party, Int) => Option[Int]) extends EngagementStrategy {
  def defenderIndex(attackers: Party, defenders: Party)(attackerIndex: Int): Option[Int] =
    func(attackers, defenders, attackerIndex)
}