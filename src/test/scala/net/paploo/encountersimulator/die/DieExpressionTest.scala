package net.paploo.encountersimulator.die

import net.paploo.encountersimulator.SpecTest

class DieExpressionTest extends SpecTest {

  val sampleSize = 100

  def sampleExpectation(values: Seq[Int]): Double = values.sum / values.size.toDouble

  val sidesd6 = (1 to 6).toSeq
  val expectationd6 = sampleExpectation(Seq(1,2,3,4,5,6))

  val sides2d6 = for { x <- sidesd6; y <- sidesd6 } yield x+y
  val expectation2d6 = sampleExpectation(sides2d6)

  describe("Constant") {

    val value = 64
    val die = Constant(value)

    it("should have the value as the minimum") {
      die.minValue should === (value)
    }

    it("should have the value as the maximum") {
      die.maxValue should === (value)
    }

    it("should have the value as the expectation") {
      die.expectationValue should === (value)
    }

    it("should have no variance to generated values") {
      val values = (1 until sampleSize).map(_ => die.value)
      all(values) should be (value)
    }

  }

  describe("SidedDie") {

    val sides = 6

    describe("Standard Die") {

      val die = SidedDie(sides)

      it("should have he correct minimum") {
        die.minValue should === (1)
      }

      it("should have he correct maximum") {
        die.maxValue should === (sides)
      }

      it("should have the correct expectationValue") {
        die.expectationValue should === (expectationd6 +- 0.00001)
      }

      it("should produce values between the min and max inclusive") {
        val values = (1 until sampleSize).map(_ => die.value)
        all(values) should (be >= 1 and be <= sides)
      }

      it("should produce the appropriate sides seq") {
        die.sides should === (sidesd6)
      }

    }

    describe("Has Zero") {

      val die = SidedDie(sides, hasZero = true)

      it("should have he correct minimum") {
        die.minValue should === (0)
      }

      it("should have he correct maximum") {
        die.maxValue should === (sides-1)
      }

      it("should have the correct expectationValue") {
        die.expectationValue should === (2.5 +- 0.00001)
      }

      it("should produce values between the min and max inclusive") {
        val values = (1 until sampleSize).map(_ => die.value)
        all(values) should (be >= 0 and be <= sides-1)
      }

      it("should produce the appropriate sides seq") {
        die.sides should === (Seq(0,1,2,3,4,5))
      }

    }

  }

  describe("Sum") {

    val sides = 6

    val dice = Seq(SidedDie(sides), SidedDie(sides))

    val expr = Sum(dice)

    it("should have he correct minimum") {
      expr.minValue should === (2)
    }

    it("should have he correct maximum") {
      expr.maxValue should === (12)
    }

    it("should have the correct expectationValue") {
      expr.expectationValue should === (expectation2d6 +- 0.00001)
    }

    it("should produce values between the min and max inclusive") {
      val values = (1 until sampleSize).map(_ => expr.value)
      all(values) should (be >= 2 and be <= 12)
    }

    it("should produce significantly more means than mins") {
      val values = (1 until sampleSize).map(_ => expr.value)
      val freqs = values.groupBy(identity)
      freqs(expectation2d6.toInt).length should be > freqs(2).length
    }

  }

  describe("Group") {

    val sides = 6

    val die = SidedDie(sides)

    val expr = Group(2, die)

    it("should have he correct minimum") {
      expr.minValue should === (2)
    }

    it("should have he correct maximum") {
      expr.maxValue should === (12)
    }

    it("should have the correct expectationValue") {
      expr.expectationValue should === (expectation2d6 +- 0.00001)
    }

    it("should produce values between the min and max inclusive") {
      val values = (1 until sampleSize).map(_ => expr.value)
      all(values) should (be >= 2 and be <= 12)
    }

    it("should produce significantly more means than mins") {
      val values = (1 until sampleSize).map(_ => expr.value)
      val freqs = values.groupBy(identity)
      freqs.getOrElse(expectation2d6.toInt, Nil).length should be > freqs.getOrElse(2, Nil).length
    }

  }

}