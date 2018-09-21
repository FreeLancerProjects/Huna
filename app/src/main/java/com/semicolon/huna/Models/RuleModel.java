package com.semicolon.huna.Models;

import java.io.Serializable;

public class RuleModel implements Serializable {
    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
