package com.revature.wordsaway.dtos.requests;

public class GameRequest {
    private String user1;
    private String user2;

    public String getUser1() {
        return user1;
    }

    public String getUser2() {
        return user2;
    }

    @Override
    public String toString() {
        return "GameRequest{" +
                "user1='" + user1 + '\'' +
                ", user2='" + user2 + '\'' +
                '}';
    }
}
