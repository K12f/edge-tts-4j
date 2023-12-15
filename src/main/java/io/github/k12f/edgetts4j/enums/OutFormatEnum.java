package io.github.k12f.edgetts4j.enums;


import com.alibaba.fastjson2.annotation.JSONField;

/**
 * <a href="https://learn.microsoft.com/en-us/azure/ai-services/speech-service/rest-text-to-speech?tabs=streaming#audio-outputs">...</a>
 */
public enum OutFormatEnum {
    AUDIO_24KHZ_48KBITRATE_MONO_MP3("audio-24khz-48kbitrate-mono-mp3"),
    AUDIO_24KHZ_96KBITRATE_MONO_MP3("audio-24khz-96kbitrate-mono-mp3"),
    WEBM_24KHZ_16BIT_MONO_OPUS("webm-24khz-16bit-mono-opus"),

    ;


    public final String value;

    OutFormatEnum(String value) {
        this.value = value;
    }

    @JSONField(value = true)
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
