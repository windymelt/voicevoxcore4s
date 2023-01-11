package com.github.windymelt.voicevoxcore4s

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.ptr.IntByReference
import com.sun.jna.Pointer

// cf. https://github.com/java-native-access/jna/blob/master/www/Mappings.md
// cf. https://github.com/VOICEVOX/voicevox_core/blob/0.13.0/core/src/core.h
trait Core extends Library {
  def initialize(use_gpu : Boolean, cpu_num_threads: Int = 0, load_all_models: Boolean = true): Boolean
  // int64_t -> Long
  def load_model(speaker_id: Long): Boolean
  def is_model_loaded(speaker_id: Long): Boolean
  // def finalize(): Unit // TODO: solve name conflict
  def metas(): String // JSON
  def supported_devices(): String // JSON
  def yukarin_s_forward(length: Long, phoneme_list: Array[Long], speaker_id: Array[Long], output: Array[Float]): Boolean
  // TODO: yukarin_sa_forward
  // TODO: decode_forward
  def last_error_message(): String
  def voicevox_load_openjtalk_dict(dict_path: String): Int // => VoicevoxResultCode
  def voicevox_tts(text: String, speaker_id: Long, output_binary_size: Array[Int]/* == IntByReference */ /* int* */, output_wav: Pointer)
  // TODO: voicevox_tts_from_kana
  def voicevox_wav_free(wav: Array[Byte]): Unit // should wrap this array
  def voicevox_error_result_to_message(result_code: Int): String
}

object Core {
  private val INSTANCE: Core = Native.load("core", classOf[Core])
  def apply(): Core = INSTANCE
}