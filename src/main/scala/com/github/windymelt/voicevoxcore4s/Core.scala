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
// cf. https://github.com/VOICEVOX/voicevox_core/blob/0.14.1/core/src/core.h
// cf. https://voicevox.github.io/voicevox_core/apis/c_api/voicevox__core_8h.html
trait Core extends Library {
  type VoicevoxResultCode = Int
  type VoicevoxAudioQueryOptions = Int
  type VoicevoxInitializeOptions = Int
  type VoicevoxSynthesisOptions = Int
  type VoicevoxTtsOptions = Int

  def voicevox_audio_query(
    text: String,
    speaker_id: Int,
    options: VoicevoxAudioQueryOptions, // TODO: struct
    output_audio_query_json: PointerByReference,
  ): VoicevoxResultCode

  def voicevox_audio_query_json_free(
    audio_query_json: String
  ): Unit

  def voicevox_decode(
    length: Long,
    phoneme_size: Long,
    f0: Array[Float],
    phoneme_vector: Array[Float],
    speaker_id: Long,
    output_decode_data_length: Long,
    output_decode_data: PointerByReference,
  ): VoicevoxResultCode

  def voicevox_decode_data_free(
    decode_data: Array[Float],
  ): Unit

  def voicevox_error_result_to_message(result_code: VoicevoxResultCode): String

  def voicevox_finalize(): Unit

  def voicevox_get_metas_json(): String

  def voicevox_get_supported_devices_json(): String

  def voicevox_get_version(): String

  def voicevox_initialize(
    options: VoicevoxInitializeOptions
  ): VoicevoxResultCode

  def voicevox_is_gpu_mode(): Boolean

  def voicevox_is_model_loaded(speaker_id: Int): Boolean

  def voicevox_load_model(speaker_id: Int): VoicevoxResultCode

  def voicevox_make_default_audio_query_options(): VoicevoxAudioQueryOptions

  def voicevox_make_default_initialize_options(): VoicevoxInitializeOptions

  def voicevox_make_default_synthesis_options(): VoicevoxSynthesisOptions

  def voicevox_make_default_tts_options(): VoicevoxTtsOptions

  def voicevox_predict_duration(
    length: Long,
    phoneme_vector: Array[Long],
    speaker_id: Int,
    output_predict_duration_data_length: Array[Long],
    output_predict_duration_data: PointerByReference, // float**
  ): VoicevoxResultCode

  def voicevox_predict_duration_data_free(predict_duration_data: Array[Float]): Unit

  def voicevox_predict_intonation(
    length: Long,
    vowel_phoneme_vector: Array[Long],
    consonant_phoneme_vector: Array[Long],
    start_accent_vector: Array[Long],
    end_accent_vector: Array[Long],
    start_accent_phrase_vector: Array[Long],
    end_accent_phrase_vector: Array[Long],
    speaker_id: Int,
    output_predict_intonation_data_length: Array[Long],
    output_predict_intonation_data: PointerByReference,
  ): VoicevoxResultCode

  def voicevox_synthesis(
    audio_query_json: String,
    speaker_id: Int,
    options: VoicevoxSynthesisOptions,
    output_wav_length: Array[Long],
    output_wav: PointerByReference,
  ): VoicevoxResultCode

  def voicevox_tts(
    text: String,
    speaker_id: Int,
    options: VoicevoxTtsOptions,
    output_wav_length: Array[Long],
    output_wav: PointerByReference,
  ): VoicevoxResultCode

  def voicevox_wav_free(wav: Array[Byte]): Unit
}

object Core {
  private val functionMap = new com.sun.jna.FunctionMapper {
    def getFunctionName(library: NativeLibrary, method: Method): String =
      method.getName() match {
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
