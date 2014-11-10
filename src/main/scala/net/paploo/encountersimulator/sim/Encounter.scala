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
  def friends: Seq[Creature]
  def foes: Seq[Creature]

  def friendsEngagementStrategy: EngagementStrategy
  def foesEngagementStrategy: EngagementStrategy

  def turn: Int
  def isFinished: Boolean = !friends.exists(_.isAlive) && !foes.exists(_.isAlive)

  def step: Encounter

  protected def attackResults(attackers: Seq[Creature], defenders: Seq[Creature])(engagementStrategy: EngagementStrategy): Seq[Creature] =
    applyDamage(defenders)(damageMap(attackIndexMap(attackers, defenders)(engagementStrategy), attackers))

  /** Construct a map of attacker indexes to the defender indexes they are attacking. **/
  private def attackIndexMap(attackers: Seq[Creature], defenders: Seq[Creature])(engagementStrategy: EngagementStrategy): Map[Int,Int] = Map()

  /** Convert the attack map to an index of defender index to the damage they take. **/
  private def damageMap(attackIndexMap: Map[Int, Int], attackers: Seq[Creature]): Map[Int, Int] = Map()

  /**
   * Given a list of defenders, apply a damage map, generating a new list of creatures with
   * damage applied.
   */
  private def applyDamage(defenders: Seq[Creature])(damageMap: Map[Int, Int]): Seq[Creature] = Seq()
}

class BasicEncounter(
                      val friends: Seq[Creature],
                      val foes: Seq[Creature],
                      val turn: Int = 0,
                      val engagementStrategy: EngagementStrategy
                    ) extends Encounter {
  override val friendsEngagementStrategy: EngagementStrategy = engagementStrategy
  override val foesEngagementStrategy: EngagementStrategy = engagementStrategy

  override val isFinished: Boolean = super.isFinished

  override def step: Encounter = {
    val steppedFriends: Seq[Creature] = attackResults(friends, foes)(friendsEngagementStrategy)
    val steppedFoes: Seq[Creature] = attackResults(foes, friends)(foesEngagementStrategy)
    new BasicEncounter(steppedFriends, steppedFoes, turn+1, engagementStrategy)
  }
}