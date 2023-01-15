package com.github.windymelt.voicevoxcore4s

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import com.sun.jna.ptr.{PointerByReference, IntByReference}

class HelloSpec extends AnyFlatSpec with Matchers {
  "Core" should "work at voicevox_tts" in {
    val core = Core()
    val initialized = core.initialize(use_gpu = false)
    initialized shouldBe true
    val loadDictResult = core.voicevox_load_openjtalk_dict("open_jtalk_dic_utf_8-1.11")
    loadDictResult shouldBe Core.VoiceVoxResultCode.VOICEVOX_RESULT_SUCCEED
    val (length, pbr) = (new IntByReference(), new PointerByReference())
    core.voicevox_tts("こんにちは、世界", 2L /* 四国めたん */, length, pbr)
    length.getValue() shouldNot be(0)
    val ptr = pbr.getValue()
    // use ptr.getByteArray(0, length.getValue())
    core.voicevox_wav_free(ptr)
    core.finalizeCore()
  }
}
