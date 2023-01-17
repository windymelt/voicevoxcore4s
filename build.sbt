import Dependencies._

ThisBuild / scalaVersion := "2.13.8"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.github.windymelt"
ThisBuild / organizationName := "windymelt"

lazy val root = (project in file("."))
  .settings(
    name := "voicevoxcore4s",
    libraryDependencies += scalaTest % Test,
    libraryDependencies ++= Seq(
      "net.java.dev.jna" % "jna" % "5.12.1",
      "net.java.dev.jna" % "jna-platform" % "5.12.1"
    ),
    Compile / unmanagedResourceDirectories += { baseDirectory.value / "open_jtalk_dic_utf_8-1.11" },
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
