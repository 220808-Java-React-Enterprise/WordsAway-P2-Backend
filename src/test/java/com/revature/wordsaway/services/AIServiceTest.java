package com.revature.wordsaway.services;

import com.revature.wordsaway.dtos.requests.BoardRequest;
import com.revature.wordsaway.models.Board;
import com.revature.wordsaway.models.User;
import com.revature.wordsaway.repositories.BoardRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Arrays;
import java.util.UUID;

import static com.revature.wordsaway.utils.Constants.BOARD_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class AIServiceTest {
    private BoardService boardService;
    private BoardRepository mockRepo;
    private User mockUser;
    private Board mockBoard;
    private AIService aiService;
    private BoardRequest request;
    private MockedStatic<AnagramService> anagramServiceMockedStatic;
    private final char[] letters = new char[BOARD_SIZE * BOARD_SIZE];
    private final char[] worms = new char[BOARD_SIZE * BOARD_SIZE];
    private char[] tray = new char[7];

    @BeforeEach
    public void setupTest(){
        Arrays.fill(letters, '.');
        Arrays.fill(worms, '.');
        for (int i = 0; i < tray.length; i++)
            tray[i] = boardService.getRandomChar();

        mockRepo = mock(BoardRepository.class);
        boardService = new BoardService(mockRepo);
        mockUser = new User("RJamesRJ", "password1", "salt", "email@domain.com", 1000, 0, 0, false);
        mockBoard = new Board(UUID.fromString("00000000-0000-0000-0000-000000000000"),
                mockUser, tray, 0, worms, letters, UUID.fromString("00000000-0000-0000-0000-000000000000"), true);
        request = mock(BoardRequest.class);
    }

    private char[] setupBoardTwentyMovesIn(){
        return (new char[]{
                '.', '.', '.', '.', '.', '.', '.', '.', 'V', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', '.', '.', 'I', '.', 'I', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', 'W', '.', 'R', '.', 'N', '.', 'A', 'C', 'T', '.', '.', '.',
                '.', 'T', '.', '.', 'A', '.', 'E', 'M', '.', '.', '.', '.', '.', 'E', '.', '.',
                '.', 'A', '.', '.', 'P', '.', '.', 'E', '.', '.', '*', '.', '.', 'C', '.', '.',
                '.', 'N', '.', '.', '.', 'C', 'I', 'T', 'R', 'O', 'N', '.', '.', 'O', '.', '.',
                '.', '.', '.', '.', '.', 'O', '.', 'A', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', '.', 'N', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', 'J', 'O', 'E', '.', '.', 'G', 'A', 'D', 'I', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', '.', 'E', '.', '.', '.', '.', '.', 'P', 'E', 'E', '.', '.',
                '.', '.', '.', '.', '.', 'E', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', '.', 'W', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', '.', '.', 'W', '.', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', 'F', '.', 'S', '.', 'E', '.', '.', '.', '.', 'G', 'L', 'E', 'D', '.',
                '.', '.', 'I', '.', 'A', 'X', 'E', '.', 'I', 'F', 'F', '.', '.', '.', '.', '.',
                '.', '.', 'L', '.', 'G', '.', 'S', '.', '.', '.', '.', '.', '.', '.', '.', '.'
        });
    }

    @Test
    public void test_easyBot_emptyBoard(){
        aiService = new AIService(mockBoard);
        Board mockBoardEmpty = mock(Board.class);
        char[] emptyLetters = new char[BOARD_SIZE * BOARD_SIZE];
        Arrays.fill(emptyLetters, '.');
        aiService.start(System.currentTimeMillis(), 1);

        when(request.getLayout()).thenReturn(mockBoard.getLetters());
        when(request.getBoardID()).thenReturn(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        when(mockRepo.findBoardByID(any())).thenReturn(mockBoardEmpty);
        when(mockBoardEmpty.getLetters()).thenReturn(emptyLetters);

        boardService.validateMove(request);
    }

    @Test
    public void test_easyBot_twentyMovesIn(){
        Board mockBoardTwenty = mock(Board.class);
        mockBoard.setLetters(setupBoardTwentyMovesIn());
        aiService = new AIService(mockBoard);
        aiService.start(System.currentTimeMillis(), 1);

        when(request.getLayout()).thenReturn(mockBoard.getLetters());
        when(request.getBoardID()).thenReturn(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        when(mockRepo.findBoardByID(any())).thenReturn(mockBoardTwenty);
        when(mockBoardTwenty.getLetters()).thenReturn(setupBoardTwentyMovesIn());

        boardService.validateMove(request);
    }

    @Test
    public void test_setWorms(){
        aiService = new AIService(mockBoard);
        aiService.setWorms();

        // Aircraft Carrier - 5
        // Battleship - 4
        // Cruiser - 3
        // Submarine - 3
        // Destroyer - 2
        // total - 17
        int countShipLength = 0;

        for (char letter : mockBoard.getWorms()){
            if (String.valueOf(letter).matches("[A-DS]"))
                countShipLength++;
        }

        assertEquals(17, countShipLength);
    }
}