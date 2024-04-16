package com.example.boardgame_project_android;

import java.sql.Date;

public class Appointments {
    private int id;
    private String appointment;
    private int employee_id;
    private int booked;
    private int guest_id;
    private int board_game_id;
    private int number_of_players;

    public Appointments(int id, String appointment, int employee_id, int booked, int guest_id, int board_game_id, int number_of_players) {
        this.id = id;
        this.appointment = appointment;
        this.employee_id = employee_id;
        this.booked = booked;
        this.guest_id = guest_id;
        this.board_game_id = board_game_id;
        this.number_of_players = number_of_players;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public int isBooked() {
        return booked;
    }

    public void setBooked(int booked) {
        this.booked = booked;
    }

    public int getGuest_id() {
        return guest_id;
    }

    public void setGuest_id(int guest_id) {
        this.guest_id = guest_id;
    }

    public int getBoard_game_id() {
        return board_game_id;
    }

    public void setBoard_game_id(int board_game_id) {
        this.board_game_id = board_game_id;
    }

    public int getNumber_of_players() {
        return number_of_players;
    }

    public void setNumber_of_players(int number_of_players) {
        this.number_of_players = number_of_players;
    }
}

