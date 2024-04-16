package com.example.boardgame_project_android;

public class Employee {
    private int id;
    private String e_name;
    private String e_mail;
    private String e_password;

    public Employee(int id, String e_name, String e_mail, String e_password) {
        this.id = id;
        this.e_name = e_name;
        this.e_mail = e_mail;
        this.e_password = e_password;
    }

    public int getId() {
        return id;
    }

    public String getE_name() {
        return e_name;
    }
}
