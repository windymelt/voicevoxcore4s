package com.github.windymelt.voicevoxcore4s

import com.sun.jna.ptr.{PointerByReference, IntByReference, LongByReference}
import java.io.FileOutputStream

object Hello extends App {
  // TODO: Log4j
  /* Extract dictionary files from JAR into real file system */
  val dictionaryDirectory = Util.extractDictFiles()
  Util.extractAndLoadLibraries()

  val core = Core()
  println(core.voicevox_get_version())
  val initializeOptions = core.voicevox_make_default_initialize_options()
  initializeOptions.open_jtalk_dict_dir = dictionaryDirectory
  initializeOptions.acceleration_mode =
    Core.VoicevoxAccelerationMode.VOICEVOX_ACCELERATION_MODE_CPU.code
  println(initializeOptions)
  val initialized = core.voicevox_initialize(initializeOptions)
  println(s"Hello, voicevoxcore4s! initialized? -> (${initialized})")
  if (initialized == Core.VoicevoxResultCode.VOICEVOX_RESULT_OK.code) {
    val loadResult = core.voicevox_load_model(2) // metan
    println(s"model loaded: $loadResult")
    val wl = new IntByReference()
    val wav = new PointerByReference()
    val ttsOpts = core.voicevox_make_default_tts_options()
    ttsOpts.kana = false
    val tts = core.voicevox_tts(
      "こんにちは、世界",
      2,
      ttsOpts,
      wl,
      wav
    )
    if (tts == Core.VoicevoxResultCode.VOICEVOX_RESULT_OK.code) {
      println(s"length: ${wl.getValue()}")
      val resultPtr = wav.getValue()
      println("got pointer")
      val resultArray = resultPtr.getByteArray(0, wl.getValue())
      println("got array")
      val fs = new FileOutputStream("./result.wav")
      fs.write(resultArray)
      println("wrote array into file")
      fs.close()
      println("closed file")
      core.voicevox_wav_free(resultPtr)
      println("freed array")
    } else {
      println(s"tts failed: $tts")
    }

    core.voicevox_finalize()
  }
}
