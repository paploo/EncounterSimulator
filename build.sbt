name := "EncounterSimulator"

version := "0.1.0"

scalaVersion := "2.11.4"

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-target:jvm-1.7",
  "-encoding", "UTF-8",
  "-feature",
  "-optimise",
  "-Yinline-warnings"
)

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"
)

initialCommands in console += "import net.paploo.encountersimulator._;"

scalacOptions in (Compile,doc) := Seq("-groups", "-implicits")

autoAPIMappings := true
