package com.github.windymelt.voicevoxcore4s

import com.sun.jna.Library
import com.sun.jna.Library.OPTION_FUNCTION_MAPPER
import com.sun.jna.Native
import com.sun.jna.NativeLibrary
import com.sun.jna.Pointer
import com.sun.jna.ptr.IntByReference
import com.sun.jna.ptr.PointerByReference

import java.lang.reflect.Method
import scala.collection.JavaConverters._

// cf. https://github.com/java-native-access/jna/blob/master/www/Mappings.md
// cf. https://github.com/VOICEVOX/voicevox_core/blob/0.13.0/core/src/core.h
trait Core extends Library {
  type VoicevoxResultCode = Int

  def initialize(
      use_gpu: Boolean,
      cpu_num_threads: Int = 0,
      load_all_models: Boolean = true
  ): Boolean

  // int64_t -> Long
  def load_model(speaker_id: Long): Boolean

  def is_model_loaded(speaker_id: Long): Boolean

  def finalizeCore(): Unit

  def metas(): String // JSON

  def supported_devices(): String // JSON

  def yukarin_s_forward(
      length: Long,
      phoneme_list: Array[Long],
      speaker_id: Array[Long],
      output: Array[Float]
  ): Boolean

  def yukarin_sa_forward(
      length: Long,
      vowel_phoneme_list: Array[Long],
      consonant_phoneme_list: Array[Long],
      start_accent_list: Array[Long],
      end_accent_list: Array[Long],
      start_accent_phrase_list: Array[Long],
      end_accent_phrase_list: Array[Long],
      speaker_id: Array[Long],
      output: Array[Float]
  ): Boolean

  def decode_forward(
      length: Long,
      phoneme_size: Long,
      f0: Array[Float],
      phoneme: Array[Float],
      speaker_id: Array[Long],
      output: Array[Float]
  ): Boolean

  def last_error_message(): String

  def voicevox_load_openjtalk_dict(
      dict_path: String
  ): VoicevoxResultCode

  def voicevox_tts(
      text: String,
      speaker_id: Long,
      output_binary_size: IntByReference,
      output_wav: PointerByReference
  ): VoicevoxResultCode

  def voicevox_tts_from_kana(
      text: String,
      speaker_id: Long,
      output_binary_size: IntByReference,
      output_wav: PointerByReference
  ): VoicevoxResultCode

  def voicevox_wav_free(wav: Pointer): Unit

  def voicevox_error_result_to_message(result_code: VoicevoxResultCode): String
}

object Core {
  private val functionMap = new com.sun.jna.FunctionMapper {
    def getFunctionName(library: NativeLibrary, method: Method): String =
      method.getName() match {
        case "finalizeCore" =>
          "finalize" // to avoid conflicting to reserved keyword
        case otherwise => otherwise
      }
  }

  private val INSTANCE: Core = Native.load(
    "core",
    classOf[Core],
    Map(OPTION_FUNCTION_MAPPER -> functionMap).asJava
  )
  def apply(): Core = INSTANCE

  object VoiceVoxResultCode extends Enumeration {
    val VOICEVOX_RESULT_SUCCEED = 0
    val VOICEVOX_RESULT_NOT_LOADED_OPENJTALK_DICT = 1
  }
}
