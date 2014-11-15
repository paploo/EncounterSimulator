package net.paploo.encountersimulator.app

import net.paploo.encountersimulator.sim._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

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

    val f = Encounter.run(encounter) { (enc: Encounter) => println(enc)}

    f.onComplete {
      case Success(_) => println("Ran Successfully")
      case Failure(e) => println(s"ERROR: $e")
    }

    Await.ready(f, Duration.Inf)
    println("Done.")
  }

}