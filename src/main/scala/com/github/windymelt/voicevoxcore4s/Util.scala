package com.github.windymelt.voicevoxcore4s

object Util {
    /**
      * Extract dictionary files for VOICEVOX into temporary directory.
      * 
      * As usual, under /tmp is used for temporary directory.
      *
      * @return Extracted dictionary directory
      */
  def extractDictFiles(): String = {
    val tmpdir = os.temp.dir(prefix = "voicevoxcore4s", deleteOnExit = true)
    println(s"extracting dictionary files itnto $tmpdir...")
    val files = Seq(
      "char.bin",
      "COPYING",
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
}
