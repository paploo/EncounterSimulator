package net.paploo.encountersimulator.sim

import net.paploo.encountersimulator.SpecTest
import net.paploo.encountersimulator.die.Constant
import net.paploo.encountersimulator.die.DieExpression._

class PartyTest extends SpecTest with SharedPartyTestExamples {

  val template = CreatureTemplate(Some("Kerbal"), 50, d20, Constant(10), d6)

  val creatureOne = Creature(Some("Jeb"), template)
  val creatureTwo = Creature(Some("Val"), template)
  val deadCreature = Creature(Some("Bill"), template).applyDamage(template.hitPoints)
  val unknownCreature = Creature(Some("Henly"), template)

  // We build these explicitly on purpose, so that no assumptions are built into how these structures on constructed.
  val aliveCreatures: Seq[Creature] = Seq(creatureOne, creatureTwo)
  val aliveCreatureMap: Map[Creature.CreatureId, Creature] = Map(creatureOne.id -> creatureOne, creatureTwo.id -> creatureTwo)
  
  val deadCreatures: Seq[Creature] = Seq(deadCreature)
  val deadCreatureMap: Map[Creature.CreatureId, Creature] = Map(deadCreature.id -> deadCreature)

  val mixedLifeCreatures: Seq[Creature] = Seq(creatureOne, deadCreature, creatureTwo)
  val mixedLifeCreatureMap: Map[Creature.CreatureId, Creature] = Map(creatureOne.id -> creatureOne, deadCreature.id -> deadCreature, creatureTwo.id -> creatureTwo)

  describe("PartyMap") {

    describe("All Alive") {

      val party = PartyMap(Some("All Alive Party Map"), aliveCreatureMap)

      it should behave like livingParty(party)
      it should behave like partialFunctionById(party, creatureTwo, unknownCreature)
      it should behave like updatableParty(party, creatureTwo)

    }
    
    describe("All Dead") {

      val party = PartyMap(Some("All Dead Party Map"), deadCreatureMap)

      it should behave like deadParty(party)
      it should behave like partialFunctionById(party, deadCreature, unknownCreature)
      it should behave like updatableParty(party, deadCreature)
      
    }
    
    describe("Mixed") {

      val party = PartyMap(Some("Mixed Party Map"), mixedLifeCreatureMap)

      it should behave like livingParty(party)
      it should behave like partialFunctionById(party, creatureTwo, unknownCreature)
      it should behave like updatableParty(party, deadCreature)

    }

    describe("Empty") {

      val party = PartyMap(Some("Empty Party Map"), Map.empty)

      it should behave like deadParty(party)

    }

  }

  describe("PartySeq") {

    describe("All Alive") {

      val party = PartySeq(Some("All Alive Party Map"), aliveCreatures)

      it should behave like livingParty(party)
      it should behave like partialFunctionById(party, creatureTwo, unknownCreature)
      it should behave like updatableParty(party, creatureTwo)

    }

    describe("All Dead") {

      val party = PartySeq(Some("All Dead Party Map"), deadCreatures)

      it should behave like deadParty(party)
      it should behave like partialFunctionById(party, deadCreature, unknownCreature)
      it should behave like updatableParty(party, deadCreature)

    }

    describe("Mixed") {

      val party = PartySeq(Some("Mixed Party Map"), mixedLifeCreatures)

      it should behave like livingParty(party)
      it should behave like partialFunctionById(party, creatureTwo, unknownCreature)
      it should behave like updatableParty(party, deadCreature)

    }

    describe("Empty") {

      val party = PartySeq(Some("Empty Party Map"), Seq.empty)

      it should behave like deadParty(party)

    }

  }

}

trait SharedPartyTestExamples {
  self: SpecTest =>

  def livingParty(party: Party): Unit = {

    it("should be alive") {
      party should be ('alive)
    }

    it("should not be dead") {
      party shouldNot be ('dead)
    }

    it("should have living creatures") {
      party.aliveCreatures shouldNot be (empty)
    }

  }

  def deadParty(party: Party): Unit = {

    it("should not be alive") {
      party shouldNot be ('alive)
    }

    it("should be dead") {
      party should be ('dead)
    }

    it("should not have living creatures") {
      party.aliveCreatures should be (empty)
    }
  }

  def creatureClassifier(party: Party): Unit = {

    it("should have only living creatures in alive creatures list") {
      all (party.aliveCreatures) should be ('alive)
    }

    it("should have only dead creatures in dead creatures list") {
      all (party.deadCreatures) should be ('dead)
    }

  }

  def partialFunctionById(party: Party, expectedCreature: Creature, unexpectedCreature: Creature): Unit = {

    it("should act like a partial function") {
      pending
    }

  }

  def updatableParty(party: Party, updatedCreature: Creature) = {

    it("should act like an updatable party") {
      pending
    }

  }

}
