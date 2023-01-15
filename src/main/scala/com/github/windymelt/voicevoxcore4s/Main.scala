package com.github.windymelt.voicevoxcore4s

import com.sun.jna.Pointer
import com.sun.jna.Memory
import com.sun.jna.Native
import com.sun.jna.ptr.{PointerByReference, IntByReference, LongByReference}
import java.io.FileOutputStream

object Hello extends App {
  println("initializing")
  println(System.getProperty("java.library.path"))
  val core = Core()
  val initialized = core.initialize(use_gpu = false)
  println(s"Hello, voicevoxcore4s! initialized? -> ($initialized)")
  if (initialized) {
    println("reading meta info...")
    println(core.metas())
    val loadDictResult = core.voicevox_load_openjtalk_dict("open_jtalk_dic_utf_8-1.11")
    println(s"loadResult: ${loadDictResult}")
    val length = new IntByReference()
    val pbr = new PointerByReference()
    core.voicevox_tts("こんにちは、世界", 2L /* めたん */, length, pbr)
    println(s"length: ${length.getValue()}")
    println(s"err: ${core.last_error_message()}")
    val resultPtr = pbr.getValue()
    val resultArray = resultPtr.getByteArray(0, length.getValue())
    val fs = new FileOutputStream("./result.wav")
    fs.write(resultArray)
    fs.close()
    core.voicevox_wav_free(resultPtr)
    core.finalizeCore()
  }
}
