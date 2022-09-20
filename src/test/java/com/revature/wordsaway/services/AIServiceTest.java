package com.revature.wordsaway.services;

import com.revature.wordsaway.dtos.requests.BoardRequest;
import com.revature.wordsaway.models.Board;
import com.revature.wordsaway.repositories.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.revature.wordsaway.utils.Constants.BOARD_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AIServiceTest {
    private BoardService boardService;
    private BoardRepository mockRepo;
    private Board mockBoard;
    private AIService aiService;
    private MockedStatic<AnagramService> mockAnagram;
    private BoardRequest request;
    private final char[] letters = new char[BOARD_SIZE * BOARD_SIZE];
    private final char[] worms = new char[BOARD_SIZE * BOARD_SIZE];
    private char[] tray = new char[7];

    @BeforeEach
    public void setupTest(){
        Arrays.fill(letters, '.');
        Arrays.fill(worms, '.');

        mockRepo = mock(BoardRepository.class);
        boardService = new BoardService(mockRepo);

        mockBoard = mock(Board.class);
        when(mockBoard.getLetters()).thenReturn(letters);
        when(mockBoard.getWorms()).thenReturn(worms);

        request = mock(BoardRequest.class);

        when(request.getBoardID()).thenReturn(UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }

    private char[] setupBlankBoard(){
        return (new char[]{
                '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'
        });
    }

    private char[] setupBoardTwentyMovesIn(){
        return (new char[]{
                '.', '.', '.', '.', '.', '.', '.', '.', 'V', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '*', '.', '.', '.', 'I', '.', 'I', '.', '.', '.', '.', '.', '.', '.',
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

    private char[] botTwentyFirstsMove(){
        return (new char[]{
                '.', '.', '.', '.', '.', '.', '.', '.', 'V', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '*', '.', '.', '.', 'I', '.', 'I', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', 'W', '.', 'R', '.', 'N', '.', 'A', 'C', 'T', '.', '.', '.',
                '.', 'T', '.', '.', 'A', '.', 'E', 'M', '.', '.', '.', '.', '.', 'E', '.', '.',
                '.', 'A', '.', '.', 'P', '.', '.', 'E', '.', '.', '*', '.', '.', 'C', '.', '.',
                '.', 'N', '.', '.', '.', 'C', 'I', 'T', 'R', 'O', 'N', '.', '.', 'O', '.', '.',
                '.', '.', '.', '.', '.', 'O', '.', 'A', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', '.', 'N', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', 'J', 'O', 'E', '.', '.', 'G', 'A', 'D', 'I', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', '.', 'E', '.', '.', '.', '.', '.', 'P', 'E', 'E', '.', '.',
                '.', '.', '.', '.', '.', 'E', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
                'S', 'H', 'A', 'D', 'O', 'W', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', '.', '.', '.', '.', 'W', '.', '.', '.', '.', '.', '.', '.', '.', '.',
                '.', '.', 'F', '.', 'S', '.', 'E', '.', '.', '.', '.', 'G', 'L', 'E', 'D', '.',
                '.', '.', 'I', '.', 'A', 'X', 'E', '.', 'I', 'F', 'F', '.', '.', '.', '.', '.',
                '.', '.', 'L', '.', 'G', '.', 'S', '.', '.', '.', '.', '.', '.', '.', '.', '.'
        });
    }

    @Test
    public void test_easyBot_emptyBoard(){
        Board blankBoard = mock(Board.class);
        mockAnagram = mockStatic(AnagramService.class);
        mockAnagram.when(() -> AnagramService.isWord(any())).thenReturn(true);

        tray = "TESTING".toCharArray();
        when(mockBoard.getTray()).thenReturn(tray);
        aiService = new AIService(mockBoard);
        aiService.setRandomSeed(0);

        mockAnagram.when(() -> AnagramService.getAllList(anyString(), anyString(), anyInt())).thenReturn(Arrays.asList("TESTING"));
        aiService.start(System.currentTimeMillis());

        when(request.getLayout()).thenReturn(letters);
        when(mockRepo.findBoardByID(any())).thenReturn(blankBoard);
        when(blankBoard.getLetters()).thenReturn(setupBlankBoard());

        boardService.validateMove(request);
        mockAnagram.close();
    }

    @Test
    public void test_easyBot_twentyMovesIn(){
        Board twentyMoveBoard = mock(Board.class);
        mockAnagram = mockStatic(AnagramService.class);
        mockAnagram.when(() -> AnagramService.isWord(any())).thenReturn(true);

        tray = "SHADOWI".toCharArray();
        when(mockBoard.getTray()).thenReturn(tray);
        when(mockBoard.getLetters()).thenReturn(setupBoardTwentyMovesIn());
        aiService = new AIService(mockBoard);
        aiService.setRandomSeed(2);

        when(mockBoard.getFireballs()).thenReturn(3);
        mockAnagram.when(() -> AnagramService.getAllList(anyString(), anyString(), anyInt())).thenReturn(Arrays.asList("SHADOW"));
        aiService.start(System.currentTimeMillis());

        when(request.getLayout()).thenReturn(botTwentyFirstsMove());
        when(mockRepo.findBoardByID(any())).thenReturn(twentyMoveBoard);
        when(twentyMoveBoard.getLetters()).thenReturn(setupBoardTwentyMovesIn());

        boardService.validateMove(request);
        mockAnagram.close();
    }

    @Test void test_easyBot_fireball(){
        Board twentyMoveBoard = mock(Board.class);
        mockAnagram = mockStatic(AnagramService.class);
        mockAnagram.when(() -> AnagramService.isWord(any())).thenReturn(true);

        tray = "TESTING".toCharArray();
        when(mockBoard.getTray()).thenReturn(tray);
        when(mockBoard.getLetters()).thenReturn(setupBoardTwentyMovesIn());
        aiService = new AIService(mockBoard);
        aiService.setRandomSeed(3);

        when(mockBoard.getFireballs()).thenReturn(3);
        mockAnagram.when(() -> AnagramService.getAllList(anyString(), anyString(), anyInt())).thenReturn(Arrays.asList("TESTING"));
        aiService.start(System.currentTimeMillis());

        when(request.getLayout()).thenReturn(botTwentyFirstsMove());
        when(mockRepo.findBoardByID(any())).thenReturn(twentyMoveBoard);
        when(twentyMoveBoard.getLetters()).thenReturn(setupBoardTwentyMovesIn());

        boardService.validateMove(request);
        mockAnagram.close();
    }

    @Test void test_forceNoMove(){
        Board twentyMoveBoard = mock(Board.class);
        mockAnagram = mockStatic(AnagramService.class);
        mockAnagram.when(() -> AnagramService.isWord(any())).thenReturn(true);

        tray = "GGGGGGG".toCharArray();
        when(mockBoard.getTray()).thenReturn(tray);
        when(mockBoard.getLetters()).thenReturn(setupBoardTwentyMovesIn());
        aiService = new AIService(mockBoard);
        AIService mockAI = mock(AIService.class);
        aiService.setRandomSeed(4);

        when(mockBoard.getFireballs()).thenReturn(3);
        mockAnagram.when(() -> AnagramService.getAllList(anyString(), anyString(), anyInt())).thenReturn(null);
        aiService.start(System.currentTimeMillis());

        when(request.isReplacedTray()).thenReturn(true);
        when(request.getLayout()).thenReturn(setupBoardTwentyMovesIn());
        when(mockRepo.findBoardByID(any())).thenReturn(twentyMoveBoard);
        when(twentyMoveBoard.getLetters()).thenReturn(setupBoardTwentyMovesIn());

        mockAnagram.close();
    }

    @RepeatedTest(20)
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