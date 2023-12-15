package io.github.k12f.edgetts4j;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import io.github.k12f.edgetts4j.constant.Constants;
import io.github.k12f.edgetts4j.enums.*;
import io.github.k12f.edgetts4j.model.SpeechConfig;
import io.github.k12f.edgetts4j.model.SpeechModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicReference;

@AllArgsConstructor
@Slf4j
public final class EdgeTTSFacade {

    // client
    private static final HttpClient.Builder clientBuilder = HttpClient.newBuilder();

    private String ssmlHeader;
    private String ssmlSpeak;

    private EdgeTTSFacade() {
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @SneakyThrows
    public ByteBuffer text2speech() {
        var audioData = new AtomicReference<ByteBuffer>();

        try (var httpClient = clientBuilder.build()) {
            var webSocketBuilder = httpClient.newWebSocketBuilder();
            var webSocket = webSocketBuilder
                    .header("Pragma", "no-cache")
                    .header("Cache-Control", "no-cache")
                    .header("Origin", Constants.OriginChrome)
                    .header("User-Agent", Constants.UA)
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .buildAsync(URI.create(Constants.SYNTH_WS_URL), new WebSocket.Listener() {
                        @Override
                        public void onOpen(WebSocket webSocket) {
                            WebSocket.Listener.super.onOpen(webSocket);
                        }

                        @Override
                        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
//                            log.info("onText");
                            if (!data.isEmpty()) {
                                var pathString = data.toString();
                                if (pathString.contains(Constants.PATH_TURN_END_FLAG)) {
                                    // send close flag
                                    webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "ok");
                                }
                            }
                            return WebSocket.Listener.super.onText(webSocket, data, last);
                        }

                        @Override
                        public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer data, boolean last) {
//                            log.info("onBinary");

                            // https://github.com/microsoft/cognitive-services-speech-sdk-js/blob/d071d11/src/common.speech/WebsocketMessageFormatter.ts#L46

                            try {
                                // merge audio bytebuffer
                                mergeAudio(data, audioData);
                            } catch (Exception e) {
                                log.error(e.getMessage());
                            }

                            return WebSocket.Listener.super.onBinary(webSocket, data, last);
                        }

                        @Override
                        public CompletionStage<?> onPing(WebSocket webSocket, ByteBuffer message) {
                            return WebSocket.Listener.super.onPing(webSocket, message);
                        }

                        @Override
                        public CompletionStage<?> onPong(WebSocket webSocket, ByteBuffer message) {
                            return WebSocket.Listener.super.onPong(webSocket, message);
                        }

                        @Override
                        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
                            return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
                        }

                        @Override
                        public void onError(WebSocket webSocket, Throwable error) {
                            WebSocket.Listener.super.onError(webSocket, error);
                        }
                    }).get();
            webSocket.sendText(ssmlHeader, true);
            webSocket.sendText(ssmlSpeak, true);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new EdgeTTSException(ResponseCodeEnum.SPEECH_TO_TEXT_WEBSOCKET_ERROR, e.getMessage());
        }
        return audioData.get();
    }

    @SneakyThrows
    public void mergeAudio(ByteBuffer data, AtomicReference<ByteBuffer> audioData) {
        var str = new String(data.array());
        var skip = 0;
        if (str.contains("Content-Type")) {
            if (str.contains("audio/mpeg")) {
                skip = 130;
            } else if (str.contains("codec=opus")) {
                skip = 142;
            }
        } else {
            skip = 105;
        }
        if (data.limit() > skip) {
            var len = 0;
            var tmpAudioData = ByteBuffer.allocate(0);
            if (ObjectUtil.isNotEmpty(audioData.get())) {
                audioData.get().clear();
                len = audioData.get().remaining();
                tmpAudioData = audioData.get();
            }

            data.position(skip);
            var newAudioData = ByteBuffer.wrap(data.array(), data.position(), data.remaining());

            var newAudioDataTmp = ByteBuffer.allocate(len + newAudioData.remaining());
            newAudioDataTmp.put(tmpAudioData);
            newAudioDataTmp.put(newAudioData);
            audioData.set(newAudioDataTmp);
        }
    }

    /**
     * 获取支持的列表
     *
     * @return 获取支持的列表
     */
    @SneakyThrows
    public Optional<List<SpeechModel>> listVoice() {

        var request = HttpRequest.newBuilder()
                .uri(URI.create(Constants.SPEECH_URL))
                .header("Authority", "speech.platform.bing.com")
                .header("Sec-CH-UA", "\" Not;A Brand\";v=\"99\", \"Microsoft Edge\";v=\"91\", \"Chromium\";v=\"91\"")
                .header("Sec-CH-UA-Mobile", "?0")
                .header("Cache-Control", "no-cache")
                .header("User-Agent", Constants.UA)
                .header("Accept", "*/*")
                .header("Sec-Fetch-Site", "none")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Dest", "empty")
                .header("Accept-Encoding", "gzip, deflate, br")
                .header("Accept-Language", "en-US,en;q=0.9")
                .version(HttpClient.Version.HTTP_2).GET().build();

        try (var httpClient = clientBuilder.build()) {
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (ObjectUtil.isNotEmpty(response)) {
                var speechModels = JSON.parseArray(response.body(), SpeechModel.class);
                return Optional.of(speechModels);
            }
        } catch (Exception e) {
            throw new EdgeTTSException(e);
        }
        return Optional.empty();
    }

    @Getter
    public static class Builder {
        private ProxySelector proxy;

        // set ssml config -> below
        private VoiceEnum speechEnum;

        private String locale;

        private String voiceName;

        private String pitch = "";

        private String rate = "";

        private String volume = "";

        private String text;

        private OutFormatEnum outFormatEnum;

        private String ssmlHeader;

        private String ssmlSpeak;

        public Builder speech(VoiceEnum speechEnum) {
            this.speechEnum = speechEnum;
            return this;
        }

        public Builder outFormat(OutFormatEnum outFormatEnum) {
            this.outFormatEnum = outFormatEnum;
            return this;
        }

        public Builder pitch(int pitch) {
            this.pitch += pitch + "Hz";
            return this;
        }

        public Builder pitch(PitchEnum pitch) {
            this.pitch = pitch.getValue();
            return this;
        }

        public Builder rate(int rate) {
            this.rate += rate + "%";
            return this;
        }

        public Builder rate(RateEnum rate) {
            this.rate = rate.getValue();
            return this;
        }

        public Builder volume(int volume) {
            this.volume = volume + "%";
            return this;
        }

        public Builder volume(VolumeEnum volume) {
            this.volume = volume.getValue();
            return this;
        }

        @SneakyThrows
        public Builder text(String text) {
            if (ObjectUtil.isEmpty(text)) {
                throw new EdgeTTSException(ResponseCodeEnum.SPEECH_NOT_FOUND_TEXT_ERROR);
            }

            this.text = removeIncompatibleCharacters(text);

            if (ObjectUtil.isEmpty(this.text)) {
                throw new EdgeTTSException(ResponseCodeEnum.SPEECH_INVALID_TEXT_ERROR);
            }

            // set default speechEnum
            if (ObjectUtil.isEmpty(speechEnum)) {
                setlocale(VoiceEnum.ZH_CN_XIAOYINEURAL);
            } else {
                setlocale(speechEnum);
            }

            if (ObjectUtil.isEmpty(speechEnum)) {
                setVoiceName(VoiceEnum.ZH_CN_XIAOYINEURAL);
            } else {
                setVoiceName(speechEnum);
            }

            if (ObjectUtil.isEmpty(pitch)) {
                pitch(0);
            }

            if (ObjectUtil.isEmpty(rate)) {
                rate(0);
            }

            if (ObjectUtil.isEmpty(volume)) {
                volume(0);
            }

            if (ObjectUtil.isEmpty(outFormatEnum)) {
                outFormat(OutFormatEnum.AUDIO_24KHZ_48KBITRATE_MONO_MP3);
            }

            var curDate = curDate();
            setSsmlHeader(curDate);

            setSsmlSpeak(curDate);
            return this;
        }

        private void setlocale(VoiceEnum voiceEnum) {
            this.locale = voiceEnum.getLocale();
        }

        private void setVoiceName(VoiceEnum voiceEnum) {
            this.voiceName = speechEnum.getName();
        }


        /**
         * 设置代理
         *
         * @param host host
         * @param port port
         * @return EdgeTTSFacade
         */
        public Builder proxy(String host, Integer port) {
            proxy = ProxySelector.of(new InetSocketAddress(host, port));
            return this;
        }

        /**
         * @return get current date
         */
        private String curDate() {
            var sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)");
            return sdf.format(new Date());
        }

        /**
         * first: set ssml header for send websock
         *
         * @param date string
         */
        private void setSsmlHeader(@NonNull String date) {
            var speechConfig = new SpeechConfig(new SpeechConfig.Context(new SpeechConfig.Synthesis(new SpeechConfig.Audio(new SpeechConfig.Metadataoptions(true, false), outFormatEnum))));
            ssmlHeader = "X-Timestamp:" + date + "\r\n"
                    + "Content-Type:application/json; charset=utf-8\r\n"
                    + "Path:speech.config\r\n\r\n"
                    + JSON.toJSON(speechConfig) + "\n";
        }

        /**
         * second: set ssml speak for send websock
         */
        @SneakyThrows
        private void setSsmlSpeak(@NonNull String date) {
            assert locale != null;
            assert voiceName != null;
            assert pitch != null;
            assert rate != null;
            assert volume != null;

            var requestId = IdUtil.fastSimpleUUID();
            ssmlSpeak = "X-RequestId:" + requestId + "\r\n"
                    + "Content-Type:application/ssml+xml\r\n"
                    + "X-Timestamp:" + date + "Z\r\n"
                    + "Path:ssml\r\n\r\n"
                    + "<speak version='1.0' xmlns='http://www.w3.org/2001/10/synthesis' xml:lang='" + locale + "'>" + "<voice name='" + voiceName + "'><prosody pitch='" + pitch + "' rate='" + rate + "' volume='" + volume + "'>" + text + "</prosody></voice></speak>";
        }

        private String removeIncompatibleCharacters(String text) {
            var chars = text.toCharArray();

            for (int i = 0; i < chars.length; i++) {
                int code = chars[i];
                if (code <= 8 || 11 <= code && code <= 12 || 14 <= code && code <= 31) {
                    chars[i] = ' ';
                }
            }

            return new String(chars);
        }

        public EdgeTTSFacade build() {
            var edgeTTSFacade = new EdgeTTSFacade();

            if (ObjectUtil.isNotEmpty(proxy)) {
                clientBuilder.proxy(proxy);
            }

            if (ObjectUtil.isNotEmpty(ssmlHeader)) {
                edgeTTSFacade.ssmlHeader = ssmlHeader;
            }

            if (ObjectUtil.isNotEmpty(ssmlSpeak)) {
                edgeTTSFacade.ssmlSpeak = ssmlSpeak;
            }
            return edgeTTSFacade;
        }
    }
}
