package io.github.k12f.edgetts4j.samples;

import cn.hutool.core.util.IdUtil;
import io.github.k12f.edgetts4j.EdgeTTSFacade;
import io.github.k12f.edgetts4j.enums.VoiceEnum;

import java.io.FileOutputStream;

public class TTS2AudioSample {
    public static void main(String[] args) {
        var facade = EdgeTTSFacade.newBuilder()
//                .pitch(0)
//                .pitch(PitchEnum.DEFAULT)
//                .rate(0)
//                .rate(RateEnum.DEFAULT)
                .volume(100)
//                .volume(VolumeEnum.LOUD)
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
