package com.github.windymelt.voicevoxcore4s

import com.sun.jna.ptr.{PointerByReference, IntByReference, LongByReference}
import java.io.FileOutputStream

object Hello extends App {
  println("initializing")
  println(System.getProperty("java.library.path"))
  /* Extract dictionary files from JAR into real file system */
  val dictionaryDirectory = Util.extractDictFiles()
  val libs = Util.extractLibraries()
  
  // CAVEAT: Call only once
  //Util.unsafeLoadLibraries()

  Util.loadCore()
  val core = Core()
  println(core.voicevox_get_version())
  val initializeOptions = core.voicevox_make_default_initialize_options()
  initializeOptions.open_jtalk_dict_dir = dictionaryDirectory
  initializeOptions.acceleration_mode = 1
  println(initializeOptions)
  val initialized = core.voicevox_initialize(initializeOptions)
  println(s"Hello, voicevoxcore4s! initialized? -> (${initialized})")
  if (initialized == Core.VoicevoxResultCode.VOICEVOX_RESULT_OK) {
    println("reading meta info...")
    println(core.voicevox_get_metas_json())
    // val loadDictResult =
    //   core.voicevox_load_openjtalk_dict(dictionaryDirectory)
    // println(s"loadResult: ${loadDictResult}")
    val wl = new IntByReference()
    val wav = new PointerByReference()
    val ttsOpts = core.voicevox_make_default_tts_options()
    val tts = core.voicevox_tts(
      "こんにちは、世界",
      2,
      ttsOpts,
      wl,
      wav,
    )
    println(s"length: ${wl.getValue()}")
    val resultPtr = wav.getValue()
    val resultArray = resultPtr.getByteArray(0, wl.getValue())
    val fs = new FileOutputStream("./result.wav")
    fs.write(resultArray)
    fs.close()
    core.voicevox_wav_free(resultArray)
    core.voicevox_finalize()
  }
}
