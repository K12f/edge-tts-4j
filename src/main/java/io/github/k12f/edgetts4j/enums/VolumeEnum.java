package io.github.k12f.edgetts4j.enums;


import lombok.Getter;

/**
 * <a href="https://learn.microsoft.com/en-us/azure/ai-services/speech-service/speech-synthesis-markup-speech#:~:text=Optional-,volume,-Indicates%20the%20volume">...</a>
 */
@Getter
public enum VolumeEnum {
    SILENT("silent"),
    X_SOFT("x-soft"),
    SOFT("soft"),
    MEDIUM("medium"),
    LOUD("LOUD"),
    X_LOUD("x-loud"),
    DEFAULT("default"),

    ;

    private final String value;

    VolumeEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
