package com.semicolon.criuse.Models;

import java.io.Serializable;

public class ContactsModel implements Serializable{
    private String whatsapp;
    private String email;

    public String getWhatsapp() {
        return whatsapp;
    }

    public String getEmail() {
        return email;
    }
}
