package com.semicolon.huna.Models;

import java.io.Serializable;

public class ResponseModel implements Serializable {
    private int success;
    private int success_object;
    private int success_action;
    private int success_end;

    public int getSuccess_action() {
        return success_action;
    }

    public int getSuccess() {
        return success;
    }

    public int getSuccess_object() {
        return success_object;
    }

    public int getSuccess_end() {
        return success_end;
    }
}
