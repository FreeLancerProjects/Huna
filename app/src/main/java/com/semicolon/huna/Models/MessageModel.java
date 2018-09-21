package com.semicolon.huna.Models;

import java.io.Serializable;

public class MessageModel implements Serializable{
    private String id_message;
    private String room_id_fk;
    private String from_id;
    private String to_id;
    private String from_name;
    private String to_name;
    private String from_image;
    private String to_image;
    private String from_type;
    private String to_type;
    private String message;
    private String image;
    private String message_type;
    private String message_time;
    private String message_date;

    public MessageModel(String room_id_fk, String from_id, String to_id, String from_name, String to_name, String from_image, String to_image, String from_type, String to_type, String message, String image, String message_type, String message_time, String message_date) {
        this.room_id_fk = room_id_fk;
        this.from_id = from_id;
        this.to_id = to_id;
        this.from_name = from_name;
        this.to_name = to_name;
        this.from_image = from_image;
        this.to_image = to_image;
        this.from_type = from_type;
        this.to_type = to_type;
        this.message = message;
        this.image = image;
        this.message_type = message_type;
        this.message_time = message_time;
        this.message_date = message_date;
    }

    public void setId_message(String id_message) {
        this.id_message = id_message;
    }

    public void setRoom_id_fk(String room_id_fk) {
        this.room_id_fk = room_id_fk;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public void setTo_name(String to_name) {
        this.to_name = to_name;
    }

    public void setFrom_image(String from_image) {
        this.from_image = from_image;
    }

    public void setTo_image(String to_image) {
        this.to_image = to_image;
    }

    public void setFrom_type(String from_type) {
        this.from_type = from_type;
    }

    public void setTo_type(String to_type) {
        this.to_type = to_type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public void setMessage_time(String message_time) {
        this.message_time = message_time;
    }

    public void setMessage_date(String message_date) {
        this.message_date = message_date;
    }

    public String getId_message() {
        return id_message;
    }

    public String getRoom_id_fk() {
        return room_id_fk;
    }

    public String getFrom_id() {
        return from_id;
    }

    public String getTo_id() {
        return to_id;
    }

    public String getFrom_name() {
        return from_name;
    }

    public String getTo_name() {
        return to_name;
    }

    public String getFrom_image() {
        return from_image;
    }

    public String getTo_image() {
        return to_image;
    }

    public String getFrom_type() {
        return from_type;
    }

    public String getTo_type() {
        return to_type;
    }

    public String getMessage() {
        return message;
    }

    public String getImage() {
        return image;
    }

    public String getMessage_type() {
        return message_type;
    }

    public String getMessage_time() {
        return message_time;
    }

    public String getMessage_date() {
        return message_date;
    }
}
