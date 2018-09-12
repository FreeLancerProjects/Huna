package com.semicolon.criuse.Models;

import java.io.Serializable;

public class UnreadModel implements Serializable {
    private int total_unread;
    private int success_read;

    public int getTotal_unread() {
        return total_unread;
    }

    public int getSuccess_read() {
        return success_read;
    }
}
