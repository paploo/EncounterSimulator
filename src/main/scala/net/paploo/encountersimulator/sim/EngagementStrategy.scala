package net.paploo.encountersimulator.sim

import scala.util.Random

object EngagementStrategy {

  def random: EngagementStrategy = new EngagementStrategyFunction(
    (attackers, defenders, attacker) => {
      val aliveDefenders = defenders.alive
      if (aliveDefenders.members.isEmpty) None
      else {
        val len = aliveDefenders.members.length
        Some(aliveDefenders.members(Random.nextInt(len)))
      }
    }
  )

}

trait EngagementStrategy {
  def defenderPartyMember(attackers: Party, defenders: Party)(attacker: PartyMember): Option[PartyMember]
}

class EngagementStrategyFunction(val func: (Party, Party, PartyMember) => Option[PartyMember]) extends EngagementStrategy {
  def defenderPartyMember(attackers: Party, defenders: Party)(attacker: PartyMember): Option[PartyMember] =
    func(attackers, defenders, attacker)
}