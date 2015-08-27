package net.paploo.encountersimulator.sim

import net.paploo.encountersimulator.SpecTest
import net.paploo.encountersimulator.die.Constant
import net.paploo.encountersimulator.die.DieExpression._

class CreatureTest extends SpecTest with SharedCreatureTestExamples {

  val template = CreatureTemplate(Some("Kerbal"), 50, d20, Constant(10), d6)

  describe("Creature") {
    
    describe("creation") {

      describe("named creature") {

        val name = Some("Valentina")

        val creature = Creature(name, template)

        it should behave like newCreature(creature, name)
      }

      describe("unnamed creature") {

        val creature = Creature(template)

        it should behave like newCreature(creature, None)

      }

      describe("id") {

        it("should have a unique id") {
          val c1 = Creature(template)
          val c2 = Creature(template)
          c1.id should !== (c2.id)
        }

      }

    }
    
    describe("health") {
      
      describe("damage application") {
        
        val damage = template.hitPoints / 4
        
        val creature = Creature(Some("Valentina"), template)

        describe("applied once") {

          val damagedCreature = creature.applyDamage(damage)

          it should behave like copiedCreature(creature, damagedCreature, shouldTestDamage = false)

          it("should have the appropriate damage") {
            damagedCreature.damage should === (damage)
          }

        }

        describe("applied multiple times") {

          val damagedCreature = creature.applyDamage(damage).applyDamage(damage).applyDamage(damage)

          it should behave like copiedCreature(creature, damagedCreature, shouldTestDamage = false)

          it("should have the appropriate damage") {
            damagedCreature.damage should === (damage*3)
          }
        }
        
      }
      
      describe("alive") {
        val creature = Creature(Some("Jebediah"), template)

        it should behave like isAlive(creature)
      }
      
      describe("barelyDead") {
        val creature = Creature(Some("Bill"), template).applyDamage(template.hitPoints)

        it should behave like isDead(creature)
      }
      
      describe("dead") {
        val creature = Creature(Some("Bob"), template).applyDamage(template.hitPoints+1)

        it should behave like isDead(creature)
      }
      
    }
    
  }
}

trait SharedCreatureTestExamples {
  this: SpecTest =>

  def newCreature(creature: Creature, expectedName: Option[String]): Unit = {

    it("should have the expected name") {
      creature.name should === (expectedName)
    }

    it("should have no damage") {
      creature.damage should === (0)
    }

  }

  def copiedCreature(destCreature: Creature, srcCreature: Creature, shouldTestDamage: Boolean = true): Unit = {

    it("should have the same id") {
      destCreature.id should === (srcCreature.id)
    }

    it("should have the same name") {
      destCreature.name should === (srcCreature.name)
    }

    it("should have the same template") {
      destCreature.template should === (srcCreature.template)
    }

    if(shouldTestDamage) it("should have the expected damage") {
      destCreature.damage should === (srcCreature.damage)
    }

  }

  def isAlive(creature: Creature): Unit = {

    it("should be alive") {
      creature should be ('alive)
    }

    it("should not be dead") {
      creature shouldNot be ('dead)
    }

    it("should have remaining hit points") {
      creature.life should be > 0
    }

    it("should have less damage than defined hit points") {
      creature.damage should be < creature.template.hitPoints
    }

  }

  def isDead(creature: Creature): Unit = {

    it("should not be alive") {
      creature shouldNot be ('alive)
    }

    it("should be dead") {
      creature should be ('dead)
    }

    it("should have zero or negative remaining hit points") {
      creature.life should be <= 0
    }

    it("should have more or the same damage as the defined hit points") {
      creature.damage should be >= creature.template.hitPoints
    }

  }
}
