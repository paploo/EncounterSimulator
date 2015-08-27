package net.paploo.encountersimulator

import org.scalatest._

abstract class SpecTest extends FunSpec with Matchers with BeforeAndAfterEach with BeforeAndAfterAll with Inspectors with GivenWhenThen with OptionValues with TryValues