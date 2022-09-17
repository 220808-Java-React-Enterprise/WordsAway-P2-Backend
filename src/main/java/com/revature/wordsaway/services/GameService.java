package com.revature.wordsaway.services;

import com.revature.wordsaway.models.Board;
import com.revature.wordsaway.models.User;
import com.revature.wordsaway.utils.customExceptions.InvalidRequestException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GameService {
    public static List<Board> register(User userA, User userB) throws InvalidRequestException{
        List<Board> boards = new ArrayList<>();
        UUID uuid = UUID.randomUUID();
        boards.add(BoardService.register(userA, uuid, true));
        boards.add(BoardService.register(userB, uuid, false));
        return boards;
    }

    public static List<Board> getByID(UUID gameID) throws InvalidRequestException {
        return BoardService.getByGameID(gameID);
    }
}
