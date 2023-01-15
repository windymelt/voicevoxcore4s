# voicevoxcore4s
Scala wrapper for [VOICEVOX Core](https://github.com/VOICEVOX/voicevox_core)

Currently this binding supports [VOICEVOX Core v0.13.0](https://github.com/VOICEVOX/voicevox_core/tree/0.13.0).

## Prerequisite

- VOICEVOX Core v0.13.0 (`libcore.so`)
  - Download here https://github.com/VOICEVOX/voicevox_core/releases/tag/0.13.0
  - Install to `/usr/lib` (`System.getProperty("java.library.path")` should contain this path)
- ONNX runtime v1.10.0 (`libonnx*.so`)
  - Download here https://github.com/microsoft/onnxruntime/releases/tag/v1.10.0
  - Install to `/usr/lib` (`System.getProperty("java.library.path")` should contain this path)
- OpenJtalk dictionary v1.11 Binary package UTF-8
  - Download here http://downloads.sourceforge.net/open-jtalk/open_jtalk_dic_utf_8-1.11.tar.gz
  - Extract `open_jtalk_dic_utf_8-1.11.tar.gz` and place directory `open_jtalk_dic_utf_8-1.11` aside `build.sbt`

## Minimal example

```scala
val core = Core()
val initialized = core.initialize(use_gpu = false)
if (initialized) {
  val loadDictResult = core.voicevox_load_openjtalk_dict("open_jtalk_dic_utf_8-1.11")
  if (loadDictResult == 0) {
    val (length, pbr) = (new IntByReference(), new PointerByReference())
    core.voicevox_tts("こんにちは、世界", 2L /* 四国めたん */, length, pbr)
    println(s"length: ${length.getValue()}")
    println(s"err: ${core.last_error_message()}")
    val resultPtr = pbr.getValue()
    val resultArray = resultPtr.getByteArray(0, length.getValue())
    val fs = new FileOutputStream("./result.wav")
    fs.write(resultArray)
    fs.close()
    core.voicevox_wav_free(resultPtr)
  }
  core.finalizeCore()
}
```

## Supported platforms

- linux-x86_64

Other platforms are not verified but may work correctly.
