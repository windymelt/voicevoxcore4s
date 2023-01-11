package com.github.windymelt.voicevoxcore4s

import com.sun.jna.Library;
import com.sun.jna.Native;

// cf. https://github.com/VOICEVOX/voicevox_core/blob/0.13.0/core/src/core.h
trait Core extends Library {
  def initialize(use_gpu : Boolean, cpu_num_threads: Int = 0, load_all_models: Boolean = true): Boolean
}

object Core {
  private val INSTANCE: Core = Native.load("core", classOf[Core])
  def apply(): Core = INSTANCE
}