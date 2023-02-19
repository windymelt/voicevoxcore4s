# voicevoxcore4s
Scala wrapper for [VOICEVOX Core](https://github.com/VOICEVOX/voicevox_core)

Currently this binding supports [VOICEVOX Core v0.14.1](https://github.com/VOICEVOX/voicevox_core/tree/0.14.1).

## Prerequisite

- VOICEVOX Core v0.14.1 (`libcore.so`)
  - sbt will download and bundle fully automatically
- OpenJtalk dictionary v1.11 Binary package UTF-8
  - Use bundled dicectory (You have to do nothing)

## Minimal example

```scala
import com.sun.jna.ptr.{PointerByReference, IntByReference}
import java.io.FileOutputStream

val dictionaryDirectory = Util.extractDictFiles()
Util.extractModels()
Util.extractAndLoadLibraries()

val core = Core()

val initializeOptions = core.voicevox_make_default_initialize_options()
initializeOptions.open_jtalk_dict_dir = dictionaryDirectory
initializeOptions.acceleration_mode =
  Core.VoicevoxAccelerationMode.VOICEVOX_ACCELERATION_MODE_CPU.code

val initialized = core.voicevox_initialize(initializeOptions)
if (initialized == Core.VoicevoxResultCode.VOICEVOX_RESULT_OK.code) {
  val loadResult = core.voicevox_load_model(2) // metan
  val (wl, wav) = (new IntByReference(), new PointerByReference())
  val ttsOpts = core.voicevox_make_default_tts_options()
  ttsOpts.kana = false

  val tts = core.voicevox_tts(
    "こんにちは、世界",
    2, // metan
    ttsOpts,
    wl,
    wav
  )
  if (tts == Core.VoicevoxResultCode.VOICEVOX_RESULT_OK.code) {
    val resultPtr = wav.getValue()
    val resultArray = resultPtr.getByteArray(0, wl.getValue())
    val fs = new FileOutputStream("./result.wav")
    fs.write(resultArray)
    fs.close()
    core.voicevox_wav_free(resultPtr)
  } else {
    println(s"tts failed: $tts")
  }

  core.voicevox_finalize()
}
```

## Supported platforms

- linux-x86_64

Other platforms are not verified but may work correctly.

## Build

```sh
sbt assembly
```
## TODO

- 公式のダウンロードスクリプトを利用する
- マルチアーキテクチャ(OS/GPU)対応
- openjtalkを圧縮格納する

## Copyright / License(s)

### libcore

This software packages and redistributes [VOICEVOX Core](https://github.com/VOICEVOX/voicevox_core) library for JAR.

Original license follows below:

```
これは VOICEVOX コアライブラリです。
https://github.com/Hiroshiba/voicevox_core

## 許諾内容

1. 商用・非商用問わず利用することができます
2. アプリケーションに組み込んで再配布することができます
3. 作成された音声を利用する際は、各音声ライブラリの規約に従ってください
4. 作成された音声の利用を他者に許諾する際は、当該他者に対し本許諾内容の 3 及び 4 の遵守を義務付けてください

## 音声ライブラリの利用規約

### 四国めたん

四国めたんの音声ライブラリを用いて生成した音声は、
「VOICEVOX:四国めたん」とクレジットを記載すれば、商用・非商用で利用可能です。

利用規約の詳細は以下をご確認ください。
https://zunko.jp/con_ongen_kiyaku.html

### ずんだもん

ずんだもんの音声ライブラリを用いて生成した音声は、
「VOICEVOX:ずんだもん」とクレジットを記載すれば、商用・非商用で利用可能です。

利用規約の詳細は以下をご確認ください。
https://zunko.jp/con_ongen_kiyaku.html

### 春日部つむぎ

春日部つむぎの音声ライブラリを用いて生成した音声は、
「VOICEVOX:春日部つむぎ」とクレジットを記載すれば、商用・非商用で利用可能です。

利用規約の詳細は以下をご確認ください。
https://tsumugi-official.studio.site/rule

### 波音リツ

波音リツの音声ライブラリを用いて生成した音声は、
「VOICEVOX:波音リツ」とクレジットを記載すれば、商用・非商用で利用可能です。

利用規約の詳細は以下をご確認ください。
http://canon-voice.com/kiyaku.html

### 玄野武宏

玄野武宏の音声ライブラリを用いて生成した音声は、
「VOICEVOX:玄野武宏」とクレジットを記載すれば、商用・非商用で利用可能です。

利用規約の詳細は以下をご確認ください。
https://virvoxproject.wixsite.com/official/voicevoxの利用規約

### 白上虎太郎

白上虎太郎の音声ライブラリを用いて生成した音声は、
「VOICEVOX:白上虎太郎」とクレジットを記載すれば、商用・非商用で利用可能です。

利用規約の詳細は以下をご確認ください。
https://virvoxproject.wixsite.com/official/voicevoxの利用規約

### 青山龍星

個人が青山龍星の音声ライブラリを用いて生成した音声は、
「VOICEVOX:青山龍星」とクレジットを記載すれば、商用・非商用で利用可能です。
ただし企業が携わる形で利用する場合は、「ななはぴ(https://v.seventhh.com/contact/)」に対し事前確認を取る必要があります。

利用規約の詳細は以下をご確認ください。
https://virvoxproject.wixsite.com/official/voicevoxの利用規約

### 冥鳴ひまり

冥鳴ひまりの音声ライブラリを用いて生成した音声は、
「VOICEVOX:冥鳴ひまり」とクレジットを記載すれば、商用・非商用で利用可能です。

利用規約の詳細は以下をご確認ください。
https://meimeihimari.wixsite.com/himari/terms-of-use

### 九州そら

九州そらの音声ライブラリを用いて生成した音声は、
「VOICEVOX:九州そら」とクレジットを記載すれば、商用・非商用で利用可能です。

利用規約の詳細は以下をご確認ください。
https://zunko.jp/con_ongen_kiyaku.html

### もち子さん

もち子さんの音声ライブラリを用いて生成した音声は、
「VOICEVOX:もち子(cv 明日葉よもぎ)」とクレジットを記載すれば、
音声作品・音声素材・ゲーム作品を除いて商用・非商用で利用可能です。

利用規約の詳細は以下をご確認ください。
https://vtubermochio.wixsite.com/mochizora/利用規約

### 剣崎雌雄

剣崎雌雄の音声ライブラリを用いて生成した音声は、
「VOICEVOX:剣崎雌雄」とクレジットを記載すれば、商用・非商用で利用可能です。

利用規約の詳細は以下をご確認ください。
https://frontier.creatia.cc/fanclubs/413/posts/4507

### WhiteCUL

WhiteCULの音声ライブラリを用いて生成した音声は、
「VOICEVOX:WhiteCUL」とそれぞれクレジットを記載すれば、商用・非商用で利用可能です。

利用規約の詳細は以下をご確認ください。
https://www.whitecul.com/guideline

### 後鬼

個人が後鬼の音声ライブラリを用いて生成した音声は、
「VOICEVOX:後鬼」とクレジットを記載すれば、商用・非商用で利用可能です。
ただし企業が携わる形で利用する場合は、「【鬼っ子ハンターついなちゃん】プロジェクト(https://ついなちゃん.com/mail/)」に対し事前確認を取る必要があります。

利用規約の詳細は以下をご確認ください。
https://ついなちゃん.com/voicevox_terms/

### No.7

個人がNo.7の音声ライブラリを用いて生成した音声は、
「VOICEVOX:No.7」とクレジットを記載すれば、非商用（同人利用や配信による収入はOK）で利用可能です。
その他商用利用の場合は、「No.7製作委員会(https://voiceseven.com/)」に対し事前確認を取る必要があります。

利用規約の詳細は以下をご確認ください。
https://voiceseven.com/#j0200

### ちび式じい

ちび式じいの音声ライブラリを用いて生成した音声は、
「VOICEVOX:ちび式じい」とクレジットを記載すれば、商用・非商用で利用可能です。

利用規約の詳細は以下をご確認ください。
https://docs.google.com/presentation/d/1AcD8zXkfzKFf2ertHwWRwJuQXjNnijMxhz7AJzEkaI4

### 櫻歌ミコ

櫻歌ミコの音声ライブラリを用いて生成した音声は、
「VOICEVOX:櫻歌ミコ」とクレジットを記載すれば、商用・非商用で利用可能です。

利用規約の詳細は以下をご確認ください。
https://voicevox35miko.studio.site/rule

### 小夜/SAYO

小夜/SAYOの音声ライブラリを用いて生成した音声は、
「VOICEVOX:小夜/SAYO」とクレジットを記載すれば、商用・非商用で利用可能です。

利用規約の詳細は以下をご確認ください。
https://316soramegu.wixsite.com/sayo-official/guideline

### ナースロボ＿タイプＴ

ナースロボ＿タイプＴの音声ライブラリを用いて生成した音声は、
「VOICEVOX:ナースロボ＿タイプＴ」とクレジットを記載すれば、商用・非商用で利用可能です。

利用規約の詳細は以下をご確認ください。
https://www.krnr.top/rules

## 禁止事項

- 逆コンパイル・リバースエンジニアリング及びこれらの方法の公開すること
- 製作者または第三者に不利益をもたらす行為
- 公序良俗に反する行為

## 免責事項

本ソフトウェアにより生じた損害・不利益について、製作者は一切の責任を負いません。

## その他

ご利用の際は VOICEVOX を利用したことがわかるクレジット表記が必要です。
```

### libonnxruntime

This software packages and redistributes [ONNX Runtime](https://github.com/microsoft/onnxruntime) library for JAR.

Original license follows below:

```
MIT License

Copyright (c) Microsoft Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

### Open JTalk Dictionary

This software contains binary pacakge of [Open JTalk](https://open-jtalk.sourceforge.net) Dictionary.

Original license follows below:

```
Copyright (c) 2009, Nara Institute of Science and Technology, Japan.

All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.
Neither the name of the Nara Institute of Science and Technology
(NAIST) nor the names of its contributors may be used to endorse or
promote products derived from this software without specific prior
written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

Copyright (c) 2011-2017, The UniDic Consortium
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

 * Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the
   distribution.

 * Neither the name of the UniDic Consortium nor the names of its
   contributors may be used to endorse or promote products derived
   from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

/* ----------------------------------------------------------------- */
/*           The Japanese TTS System "Open JTalk"                    */
/*           developed by HTS Working Group                          */
/*           http://open-jtalk.sourceforge.net/                      */
/* ----------------------------------------------------------------- */
/*                                                                   */
/*  Copyright (c) 2008-2016  Nagoya Institute of Technology          */
/*                           Department of Computer Science          */
/*                                                                   */
/* All rights reserved.                                              */
/*                                                                   */
/* Redistribution and use in source and binary forms, with or        */
/* without modification, are permitted provided that the following   */
/* conditions are met:                                               */
/*                                                                   */
/* - Redistributions of source code must retain the above copyright  */
/*   notice, this list of conditions and the following disclaimer.   */
/* - Redistributions in binary form must reproduce the above         */
/*   copyright notice, this list of conditions and the following     */
/*   disclaimer in the documentation and/or other materials provided */
/*   with the distribution.                                          */
/* - Neither the name of the HTS working group nor the names of its  */
/*   contributors may be used to endorse or promote products derived */
/*   from this software without specific prior written permission.   */
/*                                                                   */
/* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND            */
/* CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,       */
/* INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF          */
/* MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE          */
/* DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS */
/* BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,          */
/* EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED   */
/* TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,     */
/* DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON */
/* ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,   */
/* OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY    */
/* OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE           */
/* POSSIBILITY OF SUCH DAMAGE.                                       */
/* ----------------------------------------------------------------- */
```