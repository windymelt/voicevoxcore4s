import Dependencies._

ThisBuild / scalaVersion := "2.13.8"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.github.windymelt"
ThisBuild / organizationName := "windymelt"

lazy val downloadCore = taskKey[Unit]("Download libcore zip and extract it ./voicevox_core-*")
lazy val downloadOnnx = taskKey[Unit]("Download libonnx zip and extract it to ./libonnx-*")

Compile / compile := (Compile / compile).dependsOn(downloadCore).dependsOn(downloadOnnx).value

buildInfoPackage := "com.github.windymelt.voicevoxcore4s"

lazy val common = project
  .settings(
    name := "voicevoxcore4s",
    libraryDependencies ++= Seq(
      "net.java.dev.jna" % "jna" % "5.12.1",
      "net.java.dev.jna" % "jna-platform" % "5.12.1",
      "com.lihaoyi" %% "os-lib" % "0.7.2", // for extracting resources
    ),
  )

lazy val x8664linuxcpu = (project in file(".")).settings(
  name := "voicevoxcore4s-linux-x64-cpu",
  buildInfoKeys := Seq[BuildInfoKey]("libcoreFile" -> "libcore.so", "libonnxFile" -> "libonnxruntime.so.1.10.0"),
  libraryDependencies ++= Seq(
    scalaTest % Test,
  ),
  downloadCore := {
    if(java.nio.file.Files.notExists(new File("voicevox_core-linux-x64-cpu-0.13.0").toPath())) {
        println("[libcore] Path does not exist, downloading...")
        IO.unzipURL(new URL("https://github.com/VOICEVOX/voicevox_core/releases/download/0.13.0/voicevox_core-linux-x64-cpu-0.13.0.zip"), new File("voicevox_core-linux-x64-cpu-0.13.0"))
    } else {
        println("[libcore] Path exists, no need to download.")
    }
  },
  downloadOnnx := {
    import scala.sys.process._ 
    if (java.nio.file.Files.notExists(new File("onnxruntime-linux-x64-1.10.0").toPath())) {
        println("[libonnxruntime] Path does not exist, downloading...")
        url("https://github.com/microsoft/onnxruntime/releases/download/v1.10.0/onnxruntime-linux-x64-1.10.0.tgz") #> file("onnxruntime-linux-x64-1.10.0.tar.gz") !
        
        // ^^^ you need this empty line ^^^
        "tar zxvf onnxruntime-linux-x64-1.10.0.tar.gz" !
    } else {
        println("[libonnxruntime] Path exists, no need to download.")
    }
  },
  Compile / unmanagedResourceDirectories += { baseDirectory.value / "open_jtalk_dic_utf_8-1.11" },
  Compile / unmanagedResources ++= { Seq(
    file("voicevox_core-linux-x64-cpu-0.13.0/voicevox_core-linux-x64-cpu-0.13.0/libcore.so"),
    file("onnxruntime-linux-x64-1.10.0/lib/libonnxruntime.so.1.10.0"),
    file("onnxruntime-linux-x64-1.10.0/lib/libonnxruntime.so"),
  ) },
).dependsOn(common).enablePlugins(BuildInfoPlugin)
// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
