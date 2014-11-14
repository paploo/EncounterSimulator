package net.paploo.encountersimulator.sim

import scala.annotation.tailrec
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Encounter {
  def run(encounter: Encounter)(hook: (Encounter) => Any): Future[Encounter] = Future { runLoop(encounter)(hook) }

  @tailrec
  def runLoop(encounter: Encounter)(hook: (Encounter) => Any): Encounter =
    if (encounter.isFinished) encounter
    else {
      Future { hook(encounter) }
      runLoop(encounter.step)(hook)
    }
}

trait Encounter {
  def friends: Party
  def foes: Party

  def friendsEngagementStrategy: EngagementStrategy
  def foesEngagementStrategy: EngagementStrategy

  def turn: Int
  def isFinished: Boolean = friends.alive.members.isEmpty || foes.alive.members.isEmpty

  def step: Encounter

  protected def attackResults(attackers: Party, defenders: Party)(engagementStrategy: EngagementStrategy): Party =
    applyDamage(defenders)(damageMap(defenseMap(attackMap(attackers, defenders)(engagementStrategy))))

  /** Construct a map of attacker to the defender they are attacking. */
  private def attackMap(attackers: Party, defenders: Party)(engagementStrategy: EngagementStrategy): Map[PartyMember, PartyMember] = {
    val pairs = for {
      attacker <- attackers.alive.members
      defenderOption = engagementStrategy.defenderPartyMember(attackers, defenders)(attacker)
      if defenderOption.isDefined
      defender = defenderOption.get
    } yield (attacker, defender)

    pairs.toMap
  }

  /** Given an attackMap, produce a map of defenders by their common attacker */

  private def defenseMap(attackMap: Map[PartyMember, PartyMember]): Map[PartyMember, Seq[PartyMember]] =
    attackMap.groupBy( _._2 ).map { case (defender, attackerMapForDefender) => defender -> attackerMapForDefender.keys.toSeq}

  /** Convert the defense map to a defender and the total damage they take from their attackers. */
  private def damageMap(defenseMap: Map[PartyMember, Seq[PartyMember]]): Map[PartyMember, Int] =
    defenseMap.map { case (defender, attackers) => defender -> attackers.map(_.creature.damageRate).sum}


  /**
   * Given a list of defenders, apply a damage map, generating a new list of creatures with
   * damage applied.
   *
   * All creatures in the original Party must be present in the resulting Party.
   */
  private def applyDamage(defenders: Party)(damageMap: Map[PartyMember, Int]): Party =
    defenders.applyDamage(damageMap)
}

class BasicEncounter(
                      val friends: Party,
                      val foes: Party,
                      val engagementStrategy: EngagementStrategy,
                      val turn: Int = 0
                    ) extends Encounter {
  override val friendsEngagementStrategy: EngagementStrategy = engagementStrategy
  override val foesEngagementStrategy: EngagementStrategy = engagementStrategy

  override val isFinished: Boolean = super.isFinished

  override def step: Encounter = {
    val steppedFriends: Party = attackResults(friends, foes)(friendsEngagementStrategy)
    val steppedFoes: Party = attackResults(foes, friends)(foesEngagementStrategy)
    new BasicEncounter(steppedFriends, steppedFoes, engagementStrategy, turn+1)
  }

  override def toString: String = s"BasicEncounter([$turn] $friends vs $foes)"
}