package com.revature.wordsaway.services;

import com.revature.wordsaway.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {
    private static UserRepository boardRepository;

    @Autowired
    public BoardService(UserRepository boardRepository){
        this.boardRepository = boardRepository;
    }


}
