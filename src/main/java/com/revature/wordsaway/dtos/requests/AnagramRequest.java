package com.revature.wordsaway.dtos.requests;

public class AnagramRequest {
    private String letters;

    public String getLetters() {
        return letters;
    }

    @Override
    public String toString() {
        return "AnagramRequest{" +
                "letters='" + letters + '\'' +
                '}';
    }
}