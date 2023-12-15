// EdgeTTSmO.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package io.github.k12f.edgetts4j.model;

import com.alibaba.fastjson2.annotation.JSONField;
import io.github.k12f.edgetts4j.enums.OutFormatEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * support langguage list
 */
@Data
public class SpeechModel implements Serializable {

    @Serial
    private static final long serialVersionUID = 669596779304217436L;

    @JSONField(name = "Name")
    private String name;

    @JSONField(name = "Locale")
    private String locale;

    @JSONField(name = "Status")
    private String status;

    @JSONField(name = "FriendlyName")
    private String friendlyName;

    @JSONField(name = "SuggestedCodec")
    private OutFormatEnum suggestedCodec;

    @JSONField(name = "ShortName")
    private String shortName;

    @JSONField(name = "Gender")
    private String gender;

    @JSONField(name = "VoiceTag")
    private VoiceTag speechTag;

    @Data
    static class VoiceTag {
        @JSONField(name = "ContentCategories")
        private List<String> speechPersonalities;

        @JSONField(name = "VoicePersonalities")
        private List<String> contentCategories;
    }
}