package net.paploo.encountersimulator.die

import net.paploo.encountersimulator.SpecTest

class DieExpressionTest extends SpecTest {

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
      val values = (1 until 100).map(_ => die.value)
      values.filterNot(_ == value) shouldBe empty
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

      it("should have he correct expectationValue") {
        die.expectationValue should === (3.5 +- 0.00001)
      }

    }

    describe("Has Zero") {

      val die = SidedDie(sides, hasZero = true)

    }

  }

}