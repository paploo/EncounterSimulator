package net.paploo.encountersimulator.die

import scala.util.Random

trait DieExpression {
  def minValue: Int
  def maxValue: Int
  def expectationValue: Double

  def value: Int
}

trait DieOp extends DieExpression

trait Die extends DieExpression {
  def sides: Seq[Int]

  override def expectationValue: Double = sides.sum / sides.length.toDouble
}

case class SidedDie(sideCount: Int, hasZero: Boolean = false) extends Die {
  override val minValue: Int = offset

  override val maxValue: Int = (sideCount - 1) + offset

  override val expectationValue: Double = (minValue + maxValue) / 2.0

  override def value: Int = Random.nextInt(sideCount) + offset

  override lazy val sides: Seq[Int] = 0 until sideCount

  private[this] val offset: Int = if(hasZero) 0 else 1
}

case class EnumeratedDie(sides: Seq[Int]) extends Die {
  override val minValue: Int = sides.min

  override val maxValue: Int = sides.max

  override val expectationValue: Double = super.expectationValue

  override def value: Int = sides(Random.nextInt(sides.length))
}

case class Constant(value: Int) extends DieExpression {
  override val minValue: Int = value

  override val maxValue: Int = value

  override val expectationValue: Double = value.toDouble
}

case class Sum(exprs: Seq[DieExpression]) extends DieOp {
  override lazy val minValue = exprs.map(_.minValue).sum

  override lazy val maxValue = exprs.map(_.maxValue).sum

  //Expectation Value Rules http://wat2146.ucr.edu/multivariate/Expectations.pdf
  // E(a*x) = a * E(x)
  // E(x+a) = E(x) + a
  // E(x+y) = E(x) + E(y)
  override lazy val expectationValue: Double = exprs.map(_.expectationValue).sum

  override def value: Int = exprs.map(_.value).sum
}

case class Group(count: Int, expr: DieExpression) extends DieOp {
  override val minValue = count * expr.minValue

  override val maxValue = count * expr.maxValue

  override val expectationValue: Double = count * expr.expectationValue

  override def value: Int = (0 until count).map(_ => expr.value).sum
}