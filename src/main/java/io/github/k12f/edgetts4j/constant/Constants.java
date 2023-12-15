package io.github.k12f.edgetts4j.constant;

public interface Constants {

    String TRUSTED_CLIENT_TOKEN = "6A5AA1D4EAFF4E9FB37E23D68491D6F4";

    String SPEECH_URL = "https://speech.platform.bing.com/consumer/speech/synthesize/readaloud/voices/list?trustedclienttoken=" + TRUSTED_CLIENT_TOKEN;

    String SYNTH_WS_URL = "wss://speech.platform.bing.com/consumer/speech/synthesize/readaloud/edge/v1?trustedclienttoken=" + TRUSTED_CLIENT_TOKEN;
    //    String SYNTH_WS_URL = "ws://localhost:8080/chat";
    String OriginChrome = "chrome-extension://jdiccldimpdaibmpdkjnbmckianbfold";

    String UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.864.41";

    String BINARY_DELIM = "Path:audio\r\n";

    /**
     *
     */
    String PATH_TURN_START_FLAG = "Path:turn.start";
    String PATH_AUDIO_METADATA_FLAG = "Path:audio.metadata";
    String PATH_TURN_END_FLAG = "Path:turn.end";

    String SPEECH_LANG_REGEX = "\\w{2}-\\w{2}";

}
