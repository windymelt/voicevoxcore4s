package com.github.windymelt.voicevoxcore4s

import java.io.File
import java.lang.invoke.MethodHandles

object Util {
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
    println(s"extracting dictionary files into $tmpdir...")
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
    for { p <- files } {
      println(s"copying $p")
      val stream = getClass.getResourceAsStream(s"/$p")
      val tmpPath = tmpdir / p
      if (!os.exists(tmpPath)) {
        os.write(tmpPath, stream, createFolders = true)
      }
    }
    println("finished extracting")
    tmpdir.toString()
  }

  /** Extract library files for VOICEVOX into current working directory.
    *
    * @return
    *   Library directory path
    */
  def extractAndLoadLibraries(): Unit = {
    import scala.sys.process._
    import scala.collection.JavaConverters._

    System.setProperty("jna.tmpdir", libDir.toString())
    val libonnx =
      com.sun.jna.Native.extractFromResourcePath(s"/${BuildInfo.libonnxFile}")
    val targetLibonnx = new File(libonnx.getParentFile(), BuildInfo.libonnxFile)
    libonnx.renameTo(targetLibonnx)
    System.load(targetLibonnx.getAbsolutePath())

    val libcore =
      com.sun.jna.Native.extractFromResourcePath(s"/${BuildInfo.libcoreFile}")
    val targetLibcore = new File(libcore.getParentFile(), BuildInfo.libcoreFile)
    libcore.renameTo(targetLibcore)
    System.load(targetLibcore.getAbsolutePath())
    // TODO: なんとかしてmodelディレクトリをvoicevoxcore4s-libs以下にコピーする

    // val jarFile = new File(this.getClass.getProtectionDomain().getCodeSource().getLocation().getPath())
    // val jar = new JarFile(jarFile)
    // for {e <- jar.entries().asIterator().asScala} {
    //   println(e.getName())
    // }
    // println(jarFile)
  }

  /** Extract model files included in VOICEVOX Core into current working
    * directory.
    */
  def extractModels(): Unit = {
    // TODO: 自動化したい。modelディレクトリごとresourcesに格納できないだろうか
    val binaries =
      (0 to 11) flatMap (n => Seq(s"d${n}.bin", s"pd${n}.bin", s"pi${n}.bin"))
    val files = binaries ++ Seq("metas.json")
    for { p <- files } {
      val stream = getClass.getResourceAsStream(s"/$p")
      val tmpPath = libDir / "model" / p
      if (!os.exists(tmpPath)) {
        os.write(tmpPath, stream, createFolders = true)
      }
    }
  }
}
