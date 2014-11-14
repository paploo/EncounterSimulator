package net.paploo.encountersimulator.app

import net.paploo.encountersimulator.sim._

object App {

  def main(args: Array[String]): Unit = {
    println("Running...")

    val simpleTemplate = new SimpleCreatureTemplate("Simple", 4, 1)

    val friends = Party(Seq(
      SimpleCreature("A1", simpleTemplate)
    ))

    val foes = Party(Seq(
      SimpleCreature("B1", simpleTemplate),
      SimpleCreature("B2", simpleTemplate)
    ))

    val encounter = new BasicEncounter(friends, foes, EngagementStrategy.random)

    Encounter.run(encounter) { (enc: Encounter) => println(enc)}

    println("Done.")
  }

}