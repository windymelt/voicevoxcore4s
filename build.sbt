import Dependencies._
import ReleaseTransformations._

ThisBuild / scalaVersion := "2.13.13"
ThisBuild / organization := "com.github.windymelt"
ThisBuild / organizationName := "windymelt"

lazy val downloadCore =
  taskKey[Unit]("Download libcore zip and extract it ./voicevox_core-*")

Compile / compile := (Compile / compile).dependsOn(downloadCore).value

buildInfoPackage := "com.github.windymelt.voicevoxcore4s"

lazy val common = project
  .settings(
    name := "voicevoxcore4s",
    libraryDependencies ++= Seq(
      "net.java.dev.jna" % "jna" % "5.12.1",
      "net.java.dev.jna" % "jna-platform" % "5.12.1",
      "com.lihaoyi" %% "os-lib" % "0.7.2", // for extracting resources
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4" // wrapper for SLF4J
    )
  )

lazy val x8664linuxcpu = (project in file("."))
  .settings(
    name := "voicevoxcore4s-linux-x64-cpu",
    buildInfoKeys := Seq[BuildInfoKey](
      "libcoreFile" -> "libvoicevox_core.so",
      "libonnxFile" -> "libonnxruntime.so.1.13.1"
    ),
    libraryDependencies ++= Seq(
      scalaTest % Test
    ),
    downloadCore := {
      if (
        java.nio.file.Files
          .notExists(new File("voicevox_core-linux-x64-cpu-0.14.1").toPath())
      ) {
        println("[libcore] Path does not exist, downloading...")
        IO.unzipURL(
          new URL(
            "https://github.com/VOICEVOX/voicevox_core/releases/download/0.14.1/voicevox_core-linux-x64-cpu-0.14.1.zip"
          ),
          new File("voicevox_core-linux-x64-cpu-0.14.1")
        )
      } else {
        println("[libcore] Path exists, no need to download.")
      }
    },
    Compile / unmanagedResourceDirectories ++= {
      Seq(
        baseDirectory.value / "open_jtalk_dic_utf_8-1.11",
        baseDirectory.value / "voicevox_core-linux-x64-cpu-0.14.1/voicevox_core-linux-x64-cpu-0.14.1/model"
      )
    },
    Compile / unmanagedResources ++= {
      Seq(
        file(
          "voicevox_core-linux-x64-cpu-0.14.1/voicevox_core-linux-x64-cpu-0.14.1/libvoicevox_core.so"
        ),
        file(
          "voicevox_core-linux-x64-cpu-0.14.1/voicevox_core-linux-x64-cpu-0.14.1/libonnxruntime.so.1.13.1"
        )
      )
    }
  )
  .dependsOn(common)
  .enablePlugins(BuildInfoPlugin)
  .settings(
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies, // : ReleaseStep
      inquireVersions, // : ReleaseStep
      runClean, // : ReleaseStep
      runTest, // : ReleaseStep
      setReleaseVersion, // : ReleaseStep
      commitReleaseVersion, // : ReleaseStep, performs the initial git checks
      tagRelease, // : ReleaseStep
      // publishArtifacts, // : ReleaseStep, checks whether `publishTo` is properly set up
      releaseStepTask(assembly),
      setNextVersion, // : ReleaseStep
      commitNextVersion, // : ReleaseStep
      pushChanges // : ReleaseStep, also checks that an upstream branch is properly configured
    )
  )
// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
