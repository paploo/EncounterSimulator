package net.paploo.encountersimulator.sim

import net.paploo.encountersimulator.sim.Creature.CreatureId

object Encounter {

  /**
   * Tracks the state of the encounter.
   * @param turn The turn number. This should increment by 1 on each step.
   * @param currentCreatureId The id of the creature whose turn it is.
   * @param initiatives The list of the entity ids in initiative order; this can be the initial initiative or the current initiative.
   * @param parties The authoritative list of parties and their creatures.
   */
  case class EncounterState(turn: Int = 0,
                            currentCreatureId: Option[CreatureId],
                            initiatives: Seq[CreatureId],
                            parties: Seq[Party]) {
    /**
     * I had to explicitly specify the parameter type as a partial function to make this compile correctly!
     */
    val creatureMapping: PartialFunction[CreatureId, Creature] = parties.reduce[PartialFunction[CreatureId, Creature]](_ orElse _)

    def creature(id: CreatureId): Option[Creature] = if(creatureMapping.isDefinedAt(id)) Some(creatureMapping(id)) else None

    val currentParty: Option[Party] = currentCreatureId.flatMap(id => parties.find(_.isDefinedAt(id)))

    val currentCreature: Option[Creature] = currentCreatureId.flatMap(creature)

    def opposingParties: Seq[Party] = parties.filter(_.isAlive).filterNot(_ == currentParty)

    def isFinished: Boolean = currentCreature.isEmpty || opposingParties.isEmpty

    lazy val livingCreatureInitiative: Seq[CreatureId] = initiatives.filter(creature(_).map(_.isAlive).isDefined)

    val nextCreature: Option[Creature] = Some(livingCreatureInitiative.indexWhere(_ == currentCreatureId)).filter(_ >= 0).flatMap { index =>
      val nextIndex = index + 1
      val id = livingCreatureInitiative(nextIndex % livingCreatureInitiative.length)
      creature(id)
    }

  }

  sealed trait Event {
    def turn: Int
  }
  case class CreatureDied(turn: Int, id: CreatureId, creature: Creature) extends Event
  case class PartyDefeated(turn: Int, name: Option[String], party: Party) extends Event
  case class PartyWins(turn: Int, name: Option[String], party: Party) extends Event
  case class SimulationAlreadyCompleted(turn: Int) extends Event

  case class StepResult(encounterState: EncounterState, events: Seq[Event])

  /**
   * Note that we return the events generated only during this step; this way the
   * enclosing environment can decide what to do with them (e.g. save them,
   * log them, do logic based on them, send to an actore, etc).
   * @param encounterState
   * @param engagementStrategy
   * @return
   */
  def step(encounterState: EncounterState)(implicit engagementStrategy: EngagementStrategy, damageCalculator: DamageCalculator): StepResult = {
    //If there are no opposing parties or no creature selected for the turn, we are finished.
    //Select a defending creature, using the engagement strategy.
    //And process the turn, deciding on the damage caused.
    val updatedDefenderOpt: Option[Creature] = for {
      state <- if(encounterState.isFinished) None else Some(encounterState)
      attacker <- state.currentCreature
      defender <- engagementStrategy.selectDefender(attacker, state.opposingParties)
      damage <- damageCalculator.computeDamage(attacker, defender)
    } yield defender.applyDamage(damage)

    //Update the state and issue events:

    //1. Update the parties
    val updatedParties: Seq[Party] = updatedDefenderOpt match {
      case None => encounterState.parties
      case Some(updatedDefender) => encounterState.parties.map {party =>
        if(party.isDefinedAt(updatedDefender.id)) party.updated(updatedDefender) else party
      }
    }
    //2. Update the state
    val nextState: EncounterState = null

    //3. Figure out the right events
    val events: Seq[Event] = Nil

    //4. Return.
    StepResult(nextState, events)
  }


}