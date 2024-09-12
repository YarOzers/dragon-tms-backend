package com.yaroslav.dragontmsbackend.model.testcase;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.context.MessageSource;

public enum TestCaseStatus {


    READY,
    NOT_READY,
    REQUIRES_UPDATING;


    @JsonCreator
    public static TestCaseStatus fromValue(String value) {
        return switch (value.toLowerCase()) {
            case "ready" -> READY;
            case "not ready", "not_ready" -> // поддержка разных форматов
                    NOT_READY;
            case "requires_updating" -> REQUIRES_UPDATING;
            default -> throw new IllegalArgumentException("Unknown status: " + value);
        };
    }

}
