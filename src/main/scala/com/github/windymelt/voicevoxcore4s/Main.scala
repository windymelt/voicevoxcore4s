package com.github.windymelt.voicevoxcore4s

import com.sun.jna.Pointer
import com.sun.jna.Memory
import com.sun.jna.Native

object Hello extends App {
  println("initializing")
 // System.load("/usr/local/lib/libonnxruntime.so")
  //System.load("/usr/local/lib/libcore.so")
  println(System.getProperty("java.library.path"))
  val core = Core()
  val initialized = core.initialize(use_gpu = false)
  println(s"Hello, voicevoxcore4s! initialized? -> ($initialized)")
  if (initialized) {
    println("reading meta info...")
    println(core.metas())
    // めたんは2
    val length = Array[Int](42)
    val resultPtr = new Memory(Native.POINTER_SIZE).getPointer(0)
    val loadDictResult = core.voicevox_load_openjtalk_dict("open_jtalk_dic_utf_8-1.11")
    println(s"loadResult: ${loadDictResult}")
    core.voicevox_tts("こんにちは、世界", 2L, length, resultPtr)
    println(s"length: ${length.head}")
    println(s"err: ${core.last_error_message()}")
    val resultArray = resultPtr.getByteArray(0, length.head)
    core.voicevox_wav_free(resultArray) // FIXME
  }
}

