package com.revature.wordsaway.dtos.requests;

import java.util.UUID;

public class MoveRequest {
    private UUID boardID;
    private char[] move;

    public MoveRequest() {
    }

    public UUID getBoardID() {
        return boardID;
    }

    public char[] getMove() {
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
