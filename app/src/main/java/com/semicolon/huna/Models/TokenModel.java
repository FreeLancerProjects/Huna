package com.semicolon.huna.Models;

import java.io.Serializable;

public class TokenModel implements Serializable {
    private String token;

    public TokenModel(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
