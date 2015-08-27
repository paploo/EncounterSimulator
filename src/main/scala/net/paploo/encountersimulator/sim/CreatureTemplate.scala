package net.paploo.encountersimulator.sim

import net.paploo.encountersimulator.die.{Constant, DieExpression}

object CreatureTemplate {
  def apply(name: Option[String], hitPoints: Int, attack: DieExpression, defense: Int, damage: DieExpression): CreatureTemplate = apply(name, hitPoints, attack, Constant(defense), damage)

  def apply(name: Option[String], hitPoints: Int, attack: DieExpression, defense: DieExpression, damage: DieExpression): CreatureTemplate = ImmutableCreatureTemplate(name, hitPoints, attack, defense, damage)
}

trait CreatureTemplate {
  def name: Option[String]
  def hitPoints: Int
  def attack: DieExpression
  def defense: DieExpression
  def damage: DieExpression

  def difficultyScore: Double = damage.expectationValue * hitPoints
}

case class ImmutableCreatureTemplate(name: Option[String] = None,
                                  hitPoints: Int,
                                  attack: DieExpression,
                                  defense: DieExpression,
                                  damage: DieExpression) extends CreatureTemplate