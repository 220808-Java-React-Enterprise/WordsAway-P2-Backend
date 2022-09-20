package com.revature.wordsaway.dtos.responses;

import java.util.UUID;

public class OpponentResponse {
    private String username;
    private float elo;
    private UUID game_id;

    public OpponentResponse(String username, float elo, UUID game_id) {
        this.username = username;
        this.elo = elo;
        this.game_id = game_id;
    }

    public String getUsername() {
        return username;
    }

    public float getElo() {
        return elo;
    }

    public UUID getGame_id() {
        return game_id;
    }

    @Override
    public String toString() {
        return "OpponentResponse{" +
                "username='" + username + '\'' +
                ", elo=" + elo +
                ", game_id=" + game_id +
                '}';
    }
}
