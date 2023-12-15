// VoiceMetaData.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package io.github.k12f.edgetts4j.model;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * webSocket return binary
 */
@AllArgsConstructor
@Data
public class SpeechAudioMetaData implements Serializable {
    @Serial
    private static final long serialVersionUID = -7808358896005295884L;

    @JSONField(name = "Metadata")
    private List<MetaData> metaData;

    @AllArgsConstructor
    @Data
    public static class MetaData {
        @JSONField(name = "Type")
        private String type;
        @JSONField(name = "Data")
        private DataBoundary data;

    }

    @AllArgsConstructor
    @Data
    public static class DataBoundary {
        @JSONField(name = "Offset")
        private long offset;

        @JSONField(name = "Duration")
        private long duration;

        @JSONField(name = "text")
        private DataBoundaryText text;
    }

    @AllArgsConstructor
    @Data
    public static class DataBoundaryText {
        @JSONField(name = "Text")
        private String text;

        @JSONField(name = "Length")
        private long length;

        @JSONField(name = "BoundaryType")
        private String boundaryType;
    }
}
