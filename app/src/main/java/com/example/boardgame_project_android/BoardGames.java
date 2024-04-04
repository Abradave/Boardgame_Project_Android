package com.example.boardgame_project_android;

import java.util.Date;

public class BoardGames {
    private int id;
    private String bg_name;
    private int min_players;
    private int max_players;
    private String description;
    private Date created_at;
    private Date updated_at;

    public BoardGames(int id, String bg_name, int min_players, int max_players, String description) {
        this.id = id;
        this.bg_name = bg_name;
        this.min_players = min_players;
        this.max_players = max_players;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getBg_name() {
        return bg_name;
    }

    public int getMin_players() {
        return min_players;
    }

    public int getMax_players() {
        return max_players;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }
}

