
# edge-tts-4j

`edge-tts-4j` is a Java SDK that enables you to integrate Microsoft Edge's online text-to-speech service into your Java applications seamlessly.

## Main Features

-   Utilize Microsoft Edge's powerful online text-to-speech service directly in your Java projects.

## How to Use

### 1. Get Microsoft Edge's Online Text-to-Speech Services

```java
public class TTSSpeechSample {
    public static void main(String[] args) {
        var builder = EdgeTTSFacade.newBuilder();

        var facade = builder.build();

        var t1 = System.currentTimeMillis();
        var list = facade.listVoice().get();

        System.out.println(list);
        var t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);

    }
}

```
This code snippet demonstrates how to retrieve a list of available voices from Microsoft Edge's online text-to-speech service.

### 2. Convert Text to Speech

```java
public class TTS2AudioSample {
   public static void main(String[] args) {
      var facade = EdgeTTSFacade.newBuilder()
//                .pitch(0)
//                .rate(0)
              .volume(100)
              .speech(VoiceEnum.ZH_CN_XIAOXIAONEURAL)
//                .outFormat(OutFormatEnum.AUDIO_24KHZ_48KBITRATE_MONO_MP3)
              .text("你可将此文本替换为所需的任何文本。你可在此文本框中编写或在此处粘贴你自己的文本。\n" +
                      "试用不同的语言和声音。改变语速和音调。\n" +
                      "部分语音无法使用模仿与感情功能，可使用晓墨体验感情功能，使用晓晓体验模仿功能。\n" +
                      "请尽情使用文本转语音功能！\n")
              .build();

      var buffer = facade.text2speech();

      try (
              var f = new FileOutputStream(IdUtil.fastSimpleUUID() + ".mp3")
      ) {
         f.write(buffer.array());
         f.flush();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
```
This code snippet demonstrates how to convert text to speech using Microsoft Edge's online text-to-speech service. Customize parameters such as volume, language, and text content according to your needs.

Feel free to contribute, report issues, or explore additional functionalities!

## Thanks
- 1.[rany2/edge-tts](https://github.com/rany2/edge-tts)
- 2.[ChatGPT](https://chat.openai.com/)
- 3.[tts-edge-java](https://github.com/WhiteMagic2014/tts-edge-java)