package com.semicolon.criuse.Models;

import java.io.Serializable;

public class ResponseModel implements Serializable {
    private int success;
    private int success_object;
    public int getSuccess() {
        return success;
    }

    public int getSuccess_object() {
        return success_object;
    }
}
