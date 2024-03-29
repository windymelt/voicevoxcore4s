package dev.capslock.voicevoxcore4s

import java.io.File
import java.lang.invoke.MethodHandles
import Logger.logger

object Util {

  /** Extract dictionary files for VOICEVOX into temporary directory.
    *
    * As usual, under /tmp is used for temporary directory.
    *
    * @return
    *   Extracted dictionary directory
    */
  def extractDictFiles(): String = {
    val temporalDirectory =
      os.temp.dir(prefix = "voicevoxcore4s", deleteOnExit = true)
    logger.debug(s"extracting dictionary files into $temporalDirectory...")
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
      val stream = getClass.getResourceAsStream(s"/$p")
      val tmpPath = temporalDirectory / p
      if (!os.exists(tmpPath)) {
        logger.debug(s"copying $p")
        os.write(tmpPath, stream, createFolders = true)
      }
    }
    logger.debug("finished extracting")
    temporalDirectory.toString()
  }
}
