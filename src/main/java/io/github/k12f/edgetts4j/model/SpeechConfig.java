// SpeechConfig.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package io.github.k12f.edgetts4j.model;

import io.github.k12f.edgetts4j.enums.OutFormatEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * {"context":{"synthesis":{"audio":
 * {"metadataoptions":{"sentenceBoundaryEnabled":"false","wordBoundaryEnabled":"true"},
 * "outputFormat":"audio-24khz-48kbitrate-mono-mp3"}}}}
 */
@AllArgsConstructor
@Data
public class SpeechConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = 880629745537436534L;

    private Context context;

    @AllArgsConstructor
    @Data
    public static class Context {
        private Synthesis synthesis;
    }

    @AllArgsConstructor
    @Data
    public static class Synthesis {
        private Audio audio;
    }

    @AllArgsConstructor
    @Data
    public static class Audio {
        private Metadataoptions metadataoptions;
        private OutFormatEnum outputFormat;
    }

    @AllArgsConstructor
    @Data
    public static class Metadataoptions {
        private boolean wordBoundaryEnabled;
        private boolean sentenceBoundaryEnabled;
    }
}