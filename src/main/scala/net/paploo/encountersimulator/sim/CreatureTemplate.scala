package net.paploo.encountersimulator.sim

trait CreatureTemplate {
  def name: String
  def hitPoints: Int
  def damageRate: Int

  def difficultyScore: Int = damageRate * hitPoints
}

case class SimpleCreatureTemplate(name: String, hitPoints: Int, damageRate: Int) extends CreatureTemplate