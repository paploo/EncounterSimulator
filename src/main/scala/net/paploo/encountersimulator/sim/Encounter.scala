package net.paploo.encountersimulator.sim

import scala.annotation.tailrec
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Encounter {
  def run(encounter: Encounter): Future[Encounter] = Future { runLoop(encounter) }

  @tailrec
  def runLoop(encounter: Encounter): Encounter = if (encounter.isFinished) encounter else runLoop(encounter.step)
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
    applyDamage(defenders)(damageMap(attackIndexMap(attackers, defenders)(engagementStrategy), attackers))

  /** Construct a map of attacker ids to the defender ids they are attacking. **/
  private def attackIndexMap(attackers: Party, defenders: Party)(engagementStrategy: EngagementStrategy): Map[Int,Int] = Map()

  /** Convert the attack map to an index of defender index to the damage they take. **/
  private def damageMap(attackIndexMap: Map[Int, Int], attackers: Party): Map[Int, Int] = Map()

  /**
   * Given a list of defenders, apply a damage map, generating a new list of creatures with
   * damage applied.
   */
  private def applyDamage(defenders: Party)(damageMap: Map[Int, Int]): Party = Party.empty
}

class BasicEncounter(
                      val friends: Party,
                      val foes: Party,
                      val turn: Int = 0,
                      val engagementStrategy: EngagementStrategy
                    ) extends Encounter {
  override val friendsEngagementStrategy: EngagementStrategy = engagementStrategy
  override val foesEngagementStrategy: EngagementStrategy = engagementStrategy

  override val isFinished: Boolean = super.isFinished

  override def step: Encounter = {
    val steppedFriends: Party = attackResults(friends, foes)(friendsEngagementStrategy)
    val steppedFoes: Party = attackResults(foes, friends)(foesEngagementStrategy)
    new BasicEncounter(steppedFriends, steppedFoes, turn+1, engagementStrategy)
  }
}