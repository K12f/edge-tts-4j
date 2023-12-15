package io.github.k12f.edgetts4j.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCodeEnum {
    SPEECH_LIST_DATA_ERROR(4001, "get edge tts support speech list failed"),
    SPEECH_NOT_FOUND_TEXT_ERROR(4002, "please set text to speech"),
    SPEECH_INVALID_TEXT_ERROR(4003, "invalid text"),
    SPEECH_TO_TEXT_WEBSOCKET_ERROR(4004, "websocket connect failed"),
    SPEECH_TO_TEXT_AUDIO_BUFFER_ERROR(4005, "ByteBuffer error"),


    SERVER_ERROR(5000, "server error"),

    ;
    private final int code;

    private final String msg;

}
