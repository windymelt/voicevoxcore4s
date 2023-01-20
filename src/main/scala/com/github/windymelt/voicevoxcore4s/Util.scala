package com.github.windymelt.voicevoxcore4s

import java.io.File
import java.lang.invoke.MethodHandles

object Util {
  // FIXME: jarPath will diverge when using sbt...
  // val jarPath = new java.io.File(Util.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile()
  val jarPath = os.pwd
  val libDir = jarPath / "voicevoxcore4s-libs"

  /** Extract dictionary files for VOICEVOX into temporary directory.
    *
    * As usual, under /tmp is used for temporary directory.
    *
    * @return
    *   Extracted dictionary directory
    */
  def extractDictFiles(): String = {
    val tmpdir = os.temp.dir(prefix = "voicevoxcore4s", deleteOnExit = true)
    println(s"extracting dictionary files itnto $tmpdir...")
    val files = Seq(
      "char.bin",
      // "COPYING", // sbt-assemblyが自動的に名前を変更して不定になるので省略している。COPYING自体はJARに同梱される
      "left-id.def",
      "matrix.bin",
      "pos-id.def",
      "rewrite.def",
      "right-id.def",
      "sys.dic",
      "unk.dic"
    )
    for { p <- files } yield {
      println(s"copying $p")
      val stream = getClass.getResourceAsStream(s"/$p")
      val tmpPath = tmpdir / p
      os.write(tmpPath, stream)
    }
    println("finished extracting")
    tmpdir.toString()
  }

  /** Extract library files for VOICEVOX into current working directory.
    *
    * @return
    *   Library directory path
    */
  def extractLibraries(): String = {
    import scala.sys.process._

    println(s"copying library files into ${libDir.toString()}")

    // libcore
    val libcoreFile = libDir / BuildInfo.libcoreFile
    if (
      java.nio.file.Files.notExists(
        java.nio.file.Path.of(libcoreFile.toString())
      )
    ) {
      println(s"copying ${BuildInfo.libcoreFile}")
      val stream = getClass.getResourceAsStream(s"/${BuildInfo.libcoreFile}")
      os.write(libcoreFile, stream, createFolders = true)
    }

    // libonnx
    val libonnxFile = libDir / BuildInfo.libonnxFile
    if (
      java.nio.file.Files.notExists(
        java.nio.file.Path.of(libonnxFile.toString())
      )
    ) {
      println(s"copying ${BuildInfo.libonnxFile}")
      val stream = getClass.getResourceAsStream(s"/${BuildInfo.libonnxFile}")
      os.write(libonnxFile, stream, createFolders = true)
    }

    println("finished extracting")
    libDir.toString()
  }

  /** Load VOICEVOX and ONNXRuntime libraries.
    *
    * Because Double-loading causes runtime error, do call this method only once.
    */
  def unsafeLoadLibraries(): Unit = {
    System.load((libDir / BuildInfo.libcoreFile).toString)
    System.load((libDir / BuildInfo.libonnxFile).toString)
  }
}
