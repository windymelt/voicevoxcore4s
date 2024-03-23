package dev.capslock.voicevoxcore4s

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import com.sun.jna.ptr.{PointerByReference, IntByReference}

/** 確実に一度だけライブラリをロードするためにObjectを使っている
  */
object TestRunner {
  val dictionaryDirectory = Util.extractDictFiles()
  Util.extractModels()
  Util.extractAndLoadLibraries()
}

class CoreSpec extends AnyFlatSpec with Matchers {
  // "Core" should "work at voicevox_tts" in {
  //   val core = Core()
  //   val initializeOptions = core.voicevox_make_default_initialize_options()
  //   initializeOptions.open_jtalk_dict_dir = dictionaryDirectory
  //   initializeOptions.acceleration_mode =
  //     Core.VoicevoxAccelerationMode.VOICEVOX_ACCELERATION_MODE_CPU.code
  //   val initialized = core.voicevox_initialize(initializeOptions)
  //   initialized shouldBe Core.VoicevoxResultCode.VOICEVOX_RESULT_OK.code
  //   val loadResult = core.voicevox_load_model(2)
  //   loadResult shouldBe Core.VoicevoxResultCode.VOICEVOX_RESULT_OK.code
  //   val (length, pbr) = (new IntByReference(), new PointerByReference())
  //   val ttsOpts = core.voicevox_make_default_tts_options()
  //   ttsOpts.kana = false
  //   core.voicevox_tts("こんにちは、世界", 2, ttsOpts, length, pbr)
  //   length.getValue() shouldNot be(0)
  //   val ptr = pbr.getValue()
  //   core.voicevox_wav_free(ptr)
  //   core.voicevox_finalize()
  // }

  val expectedVersion = "0.14.1"
  "Core" should s"return version $expectedVersion" in {
    Core().voicevox_get_version() shouldBe expectedVersion
  }
}
