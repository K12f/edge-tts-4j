package io.github.k12f.edgetts4j.enums;

import lombok.Getter;

/**
 * <a href="https://learn.microsoft.com/en-us/azure/ai-services/speech-service/speech-synthesis-markup-speech#:~:text=Optional-,rate,-Indicates%20the%20speaking">...</a>
 * 速度
 */
@Getter
public enum RateEnum {
    X_SLOW("x-slow"),
    SLOW("slow"),
    MEDIUM("medium"),
    FAST("fast"),
    X_FAST("x-fast"),
    DEFAULT("default"),

    ;

    private final String value;

    RateEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
