package com.revature.wordsaway.services;

import com.revature.wordsaway.models.Board;
import com.revature.wordsaway.repositories.BoardRepository;
import org.junit.Before;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static com.revature.wordsaway.utils.Constants.BOARD_SIZE;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AIServiceTest {
    private BoardService boardService;
    private BoardRepository mockRepo;
    private AIService aiService;
    private Board mockBoard;

    @Before
    public void setupTest(){
    }

    @Test
    public void test_easyBot(){
    }
}