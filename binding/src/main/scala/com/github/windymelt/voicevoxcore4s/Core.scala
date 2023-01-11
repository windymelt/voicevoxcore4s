package com.github.windymelt.voicevoxcore4s

import com.github.sbt.jni.nativeLoader

//@nativeLoader("onnxruntime") object OnnxRuntime {}

@nativeLoader("native0")
class Core() {
  @native def initialize(use_gpu : Boolean, cpu_num_threads: Int = 0, load_all_models: Boolean = true): Boolean
}

