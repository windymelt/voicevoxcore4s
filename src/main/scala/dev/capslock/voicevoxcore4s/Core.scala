package dev.capslock.voicevoxcore4s

import com.sun.jna.Library
import com.sun.jna.Library.OPTION_FUNCTION_MAPPER
import com.sun.jna.Native
import com.sun.jna.NativeLibrary
import com.sun.jna.Pointer
import com.sun.jna.Structure
import com.sun.jna.ptr.IntByReference
import com.sun.jna.ptr.LongByReference
import com.sun.jna.ptr.PointerByReference

import java.lang.reflect.Method
import scala.collection.JavaConverters._
import java.{util => ju}

// cf. https://github.com/java-native-access/jna/blob/master/www/Mappings.md
// cf. https://github.com/VOICEVOX/voicevox_core/blob/0.14.1/core/src/core.h
// cf. https://voicevox.github.io/voicevox_core/apis/c_api/voicevox__core_8h.html

/** VOICEVOX CoreライブラリのJNAによるラッパー。純粋にラッパーとして振る舞うため、元ライブラリに忠実に振る舞う。
  */
trait Core extends Library {
  def voicevox_audio_query(
      text: String,
      speaker_id: Int,
      options: CoreJ.VoicevoxAudioQueryOptions.ByValue,
      output_audio_query_json: PointerByReference
  ): Core.VoicevoxResultCode.Repr

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
      output_decode_data: PointerByReference
  ): Core.VoicevoxResultCode.Repr

  def voicevox_decode_data_free(
      decode_data: Array[Float]
  ): Unit

  def voicevox_error_result_to_message(
      result_code: Core.VoicevoxResultCode.Repr
  ): String

  def voicevox_finalize(): Unit

  def voicevox_get_metas_json(): String

  def voicevox_get_supported_devices_json(): String

  def voicevox_get_version(): String

  def voicevox_initialize(
      options: CoreJ.VoicevoxInitializeOptions.ByValue
  ): Core.VoicevoxResultCode.Repr

  def voicevox_is_gpu_mode(): Boolean

  def voicevox_is_model_loaded(speaker_id: Int): Boolean

  def voicevox_load_model(speaker_id: Int): Core.VoicevoxResultCode.Repr

  def voicevox_make_default_audio_query_options()
      : CoreJ.VoicevoxAudioQueryOptions.ByValue

  def voicevox_make_default_initialize_options()
      : CoreJ.VoicevoxInitializeOptions.ByValue

  def voicevox_make_default_synthesis_options()
      : CoreJ.VoicevoxSynthesisOptions.ByValue

  def voicevox_make_default_tts_options(): CoreJ.VoicevoxTtsOptions.ByValue

  def voicevox_predict_duration(
      length: Long,
      phoneme_vector: Array[Long],
      speaker_id: Int,
      output_predict_duration_data_length: Array[Long],
      output_predict_duration_data: PointerByReference // float**
  ): Core.VoicevoxResultCode.Repr

  def voicevox_predict_duration_data_free(predict_duration_data: Pointer): Unit

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
      output_predict_intonation_data: PointerByReference
  ): Core.VoicevoxResultCode.Repr

  def voicevox_synthesis(
      audio_query_json: String,
      speaker_id: Int,
      options: CoreJ.VoicevoxSynthesisOptions.ByValue,
      output_wav_length: Array[Long],
      output_wav: PointerByReference
  ): Core.VoicevoxResultCode.Repr

  def voicevox_tts(
      text: String,
      speaker_id: Int,
      options: CoreJ.VoicevoxTtsOptions.ByValue,
      output_wav_length: IntByReference,
      output_wav: PointerByReference
  ): Core.VoicevoxResultCode.Repr

  def voicevox_wav_free(wav: Pointer): Unit
}

object Core {

  /** 関数/メソッド名を変換する層。かつてのバージョンのCoreでJavaの予約語に関数名が被っていた名残として用意している。
    */
  private val functionMap = new com.sun.jna.FunctionMapper {
    def getFunctionName(library: NativeLibrary, method: Method): String =
      method.getName() match {
        case otherwise => otherwise
      }
  }

  private lazy val INSTANCE: Core = {
    Logger.logger.debug("Loading VOICEVOX Core via JNA...")
    val core = Native.load(
      "voicevox_core",
      classOf[Core],
      Map(OPTION_FUNCTION_MAPPER -> functionMap).asJava
    )
    Logger.logger.debug("VOICEVOX Core loaded.")
    core
  }
  def apply(): Core = INSTANCE

  sealed abstract class VoicevoxResultCode(val code: Int)
  object VoicevoxResultCode {
    type Repr = Int
    case object VOICEVOX_RESULT_OK extends VoicevoxResultCode(0)
    case object VOICEVOX_RESULT_NOT_LOADED_OPENJTALK_DICT_ERROR
        extends VoicevoxResultCode(1)
    case object VOICEVOX_RESULT_LOAD_MODEL_ERROR extends VoicevoxResultCode(2)
    case object VOICEVOX_RESULT_GET_SUPPORTED_DEVICES_ERROR
        extends VoicevoxResultCode(3)
    case object VOICEVOX_RESULT_GPU_SUPPORT_ERROR extends VoicevoxResultCode(4)
    case object VOICEVOX_RESULT_LOAD_METAS_ERROR extends VoicevoxResultCode(5)
    case object VOICEVOX_RESULT_UNINITIALIZED_STATUS_ERROR
        extends VoicevoxResultCode(6)
    case object VOICEVOX_RESULT_INVALID_SPEAKER_ID_ERROR
        extends VoicevoxResultCode(7)
    case object VOICEVOX_RESULT_INVALID_MODEL_INDEX_ERROR
        extends VoicevoxResultCode(8)
    case object VOICEVOX_RESULT_INFERENCE_ERROR extends VoicevoxResultCode(9)
    case object VOICEVOX_RESULT_EXTRACT_FULL_CONTEXT_LABEL_ERROR
        extends VoicevoxResultCode(10)
    case object VOICEVOX_RESULT_INVALID_UTF8_INPUT_ERROR
        extends VoicevoxResultCode(11)
    case object VOICEVOX_RESULT_PARSE_KANA_ERROR extends VoicevoxResultCode(12)
    case object VOICEVOX_RESULT_INVALID_AUDIO_QUERY_ERROR
        extends VoicevoxResultCode(13)
  }

  sealed abstract class VoicevoxAccelerationMode(val code: Short)
  object VoicevoxAccelerationMode {
    case object VOICEVOX_ACCELERATION_MODE_AUTO
        extends VoicevoxAccelerationMode(0)
    case object VOICEVOX_ACCELERATION_MODE_CPU
        extends VoicevoxAccelerationMode(1)
    case object VOICEVOX_ACCELERATION_MODE_GPU
        extends VoicevoxAccelerationMode(2)
  }
}
