package com.semicolon.huna.Models;

import java.io.Serializable;

public class TypingModel implements Serializable {
    private String type_value;

    public TypingModel(String type_value) {
        this.type_value = type_value;
    }

    public String getType_value() {
        return type_value;
    }
}
