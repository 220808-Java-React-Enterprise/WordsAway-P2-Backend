package com.revature.wordsaway.dtos.requests;

import java.util.UUID;

public class MoveRequest {
    private String boardID;
    private String move;

    public MoveRequest() {
    }

    public String getBoardID() {
        return boardID;
    }

    public String getMove() {
        return move;
    }

    @Override
    public String toString() {
        return "MoveRequest{" +
                "boardID=" + boardID +
                ", move=" + move +
                '}';
    }
}
