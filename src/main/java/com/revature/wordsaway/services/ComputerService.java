package com.revature.wordsaway.services;

import com.revature.wordsaway.models.Board;

import java.util.List;
import java.util.Map;

public class ComputerService {
    private final Board board;

    private List<Character> additionalLetters;
    private Map<Integer, char[]> possibleWords;

    public ComputerService(Board board) {
        this.board = board;
    }


}
