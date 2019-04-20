name := "maze"

version := "0.1"

scalaVersion := "2.12.8"

scalacOptions += "-Ypartial-unification"

addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.0")
addCompilerPlugin("org.scalamacros" %% "paradise" % "2.1.0" cross CrossVersion.full)

val cats = "1.6.0"
val catsEffect = "1.2.0"
val monocleVersion = "1.5.0"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % cats,
  "org.typelevel" %% "cats-effect" % catsEffect,
  "com.github.julien-truffaut" %%  "monocle-core"  % monocleVersion,
  "com.github.julien-truffaut" %%  "monocle-macro" % monocleVersion,
)