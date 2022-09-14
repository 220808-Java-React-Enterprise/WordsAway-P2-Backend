package com.revature.wordsaway.models;
import com.revature.wordsaway.services.BoardService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game {
    private List<Board> boards;
    private User turnUser;

    public Game(User user1, User user2) {
        if(user1 == null || user2 == null || user1.equals(user2)) throw new IllegalArgumentException("Invalid users for game creation.");
        UUID gameID = UUID.randomUUID();
        boards = new ArrayList<>();
        boards.add(BoardService.register(user1, gameID, false));
        boards.add(BoardService.register(user2, gameID, true));
        turnUser = user2;
    }

    public Game(UUID gameID){
        this.boards = BoardService.getByGameID(gameID);
        turnUser = boards.get(0).isActive() ? boards.get(0).getUser() : boards.get(1).getUser();
    }

    public Board getBoard(int index){
        if(index > 1 || index < 0) throw new IllegalArgumentException("Index out of bounds.");
        return boards.get(index);
    }

    @Override
    public String toString() {
        return "Game{" +
                "boards=" + boards +
                ", turnUser=" + turnUser +
                '}';
    }
}
