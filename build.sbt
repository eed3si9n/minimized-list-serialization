ThisBuild / scalaVersion := "2.13.0-M5"

lazy val root = (project in file("."))
  .settings(
    name := "minimized-list-serialization",
    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % Test,
    // Test / fork := true,
  )
