package net.paploo.encountersimulator.sim

import scala.util.Random

object EngagementStrategy {

  def random: EngagementStrategy = new EngagementStrategyFunction(
    (attackers, defenders, attackerId) => {
      val aliveDefenders = defenders.alive
      if (aliveDefenders.members.isEmpty) None
      else {
        val len = aliveDefenders.members.length
        Some(aliveDefenders.members(Random.nextInt(len)).id)
      }
    }
  )

}

trait EngagementStrategy {
  def defenderIndex(attackers: Party, defenders: Party)(id: Int): Option[Int]
}

class EngagementStrategyFunction(val func: (Party, Party, Int) => Option[Int]) extends EngagementStrategy {
  def defenderIndex(attackers: Party, defenders: Party)(id: Int): Option[Int] =
    func(attackers, defenders, id)
}