package com.revature.wordsaway.dtos.requests;

public class UsernameRequest {
    private String username;

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "UsernameRequest{" +
                "username='" + username + '\'' +
                '}';
    }
}
