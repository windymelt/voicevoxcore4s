# voicevoxcore4s
Scala wrapper for VOICEVOX Core

## Developing notes

- install onnx runtime https://github.com/microsoft/onnxruntime/releases/tag/v1.10.0
- place onnx runtime files and `libcore.so` to `binding/src/main/resources/native/x86_64-linux/`

generate header files via `sbt javah`.
