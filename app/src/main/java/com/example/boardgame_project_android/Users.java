package com.example.boardgame_project_android;

import java.util.Date;

public class Users {
    private int id;
    private String g_username;
    private String g_password;
    private String g_name;
    private String g_email;
    private String g_phone_number;
    private Date created_at;
    private Date updated_at;

    public Users(int id, String g_username, String g_password, String g_name, String g_email, String g_phone_number) {
        this.id = id;
        this.g_username = g_username;
        this.g_password = g_password;
        this.g_name = g_name;
        this.g_email = g_email;
        this.g_phone_number = g_phone_number;
    }

    public Users(int id, String g_password, String g_email) {
        this.id = id;
        this.g_password = g_password;
        this.g_email = g_email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getG_username() {
        return g_username;
    }

    public void setG_username(String g_username) {
        this.g_username = g_username;
    }

    public String getG_password() {
        return g_password;
    }

    public void setG_password(String g_password) {
        this.g_password = g_password;
    }

    public String getG_name() {
        return g_name;
    }

    public void setG_name(String g_name) {
        this.g_name = g_name;
    }

    public String getG_email() {
        return g_email;
    }

    public void setG_email(String g_email) {
        this.g_email = g_email;
    }

    public String getG_phone_number() {
        return g_phone_number;
    }

    public void setG_phone_number(String g_phone_number) {
        this.g_phone_number = g_phone_number;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
}

