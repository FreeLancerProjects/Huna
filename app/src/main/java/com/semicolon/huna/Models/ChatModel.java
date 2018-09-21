package com.semicolon.huna.Models;

import java.io.Serializable;

public class ChatModel implements Serializable {
    private String curr_id;
    private String chat_id;
    private String curr_type;
    private String curr_image;
    private String chat_type;
    private String chat_name;
    private String Chat_image;
    private String room_id;

    public ChatModel(String curr_id, String chat_id, String curr_type, String curr_image, String chat_type, String chat_name, String chat_image, String room_id) {
        this.curr_id = curr_id;
        this.chat_id = chat_id;
        this.curr_type = curr_type;
        this.curr_image = curr_image;
        this.chat_type = chat_type;
        this.chat_name = chat_name;
        Chat_image = chat_image;
        this.room_id = room_id;
    }

    public String getCurr_id() {
        return curr_id;
    }

    public String getChat_id() {
        return chat_id;
    }

    public String getCurr_type() {
        return curr_type;
    }

    public String getCurr_image() {
        return curr_image;
    }

    public String getChat_type() {
        return chat_type;
    }

    public String getChat_name() {
        return chat_name;
    }

    public String getChat_image() {
        return Chat_image;
    }

    public String getRoom_id() {
        return room_id;
    }
}
