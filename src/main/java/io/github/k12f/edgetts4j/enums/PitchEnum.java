package io.github.k12f.edgetts4j.enums;

import lombok.Getter;

/**
 * <a href="https://learn.microsoft.com/en-us/azure/ai-services/speech-service/speech-synthesis-markup-speech#:~:text=Optional-,pitch,-Indicates%20the%20baseline">...</a>
 * 音调
 */
@Getter
public enum PitchEnum {
    X_LOW("x-low"),
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high"),
    X_HIGH("x-high"),
    DEFAULT("default"),

    ;

    private final String value;

    PitchEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
