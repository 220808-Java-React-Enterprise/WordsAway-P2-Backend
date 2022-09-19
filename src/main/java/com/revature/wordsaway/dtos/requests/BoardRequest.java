package com.revature.wordsaway.dtos.requests;

import java.util.UUID;

public class BoardRequest {
    private UUID boardID;
    private char[] layout;

    public BoardRequest() {
    }

    public UUID getBoardID() {
        return boardID;
    }

    public char[] getLayout() {
        return layout;
    }

    @Override
    public String toString() {
        return "MoveRequest{" +
                "boardID=" + boardID +
                ", move=" + layout +
                '}';
    }
}
