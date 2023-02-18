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
  def extractLibraries(): Unit = {
    import scala.sys.process._

    // TODO: attempt to use com.sun.jna.Native.extractFromResourcePath()
    System.setProperty("jna.tmpdir", libDir.toString())
    println(s"Extracting library into $libDir")
    com.sun.jna.Native.extractFromResourcePath(s"/${BuildInfo.libcoreFile}")
    com.sun.jna.Native.extractFromResourcePath(s"/${BuildInfo.libonnxFile}")

    // TODO: なんとかしてmodelディレクトリをvoicevoxcore4s-libs以下にコピーする
  }

  def loadCore(): Unit = {
    println("loading onnx")
    com.sun.jna.NativeLibrary.getInstance("libonnxruntime.so.1.13.1")
    println("loading core")
    com.sun.jna.NativeLibrary.getInstance("voicevox_core")
    println("library loaded")
  }
}
