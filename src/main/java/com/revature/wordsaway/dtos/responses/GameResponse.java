package com.revature.wordsaway.dtos.responses;

import java.util.Arrays;

public class GameResponse {
    private char[] letters;
    private char[] worms;
    private char[] tray;
    private int fireballs;
    private String opponent;

    public GameResponse(char[] letters, char[] worms, char[] tray, int fireballs, String opponent) {
        this.letters = letters;
        this.worms = worms;
        this.tray = tray;
        this.fireballs = fireballs;
        this.opponent = opponent;
    }

    public char[] getLetters() {
        return letters;
    }

    public char[] getWorms() {
        return worms;
    }

    public char[] getTray() {
        return tray;
    }

    public int getFireballs() {
        return fireballs;
    }

    public String getOpponent() {
        return opponent;
    }

    @Override
    public String toString() {
        return "GameResponse{" +
                "letters=" + Arrays.toString(letters) +
                ", worms=" + Arrays.toString(worms) +
                ", tray=" + Arrays.toString(tray) +
                ", fireballs=" + fireballs +
                ", opponent='" + opponent + '\'' +
                '}';
    }
}
