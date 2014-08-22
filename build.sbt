name := "enum-explore"

scalaVersion in ThisBuild := "2.11.2"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  // "-Xfatal-warnings", - not here because we want to observe match no exhaustive
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture"
)

libraryDependencies in ThisBuild ++= Seq(
  "org.specs2" %% "specs2" % "2.3.11" % "test",
  "org.scalaz" %% "scalaz-core" % "7.1.0",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value
)

lazy val macros = project.in(file("macros"))

lazy val enums = project.in(file(".")).dependsOn(macros)
