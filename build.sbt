name := "maze"

version := "0.1"

scalaVersion := "2.12.8"

scalacOptions += "-Ypartial-unification"

addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.0")

libraryDependencies += "org.typelevel" %% "cats-core" % "1.6.0"
libraryDependencies += "org.typelevel" %% "cats-effect" % "1.2.0"