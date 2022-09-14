package com.revature.wordsaway.services;

import com.revature.wordsaway.dtos.requests.MoveRequest;
import com.revature.wordsaway.models.Board;
import com.revature.wordsaway.repositories.BoardRepository;
import com.revature.wordsaway.utils.customExceptions.InvalidRequestException;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.util.Arrays;
import java.util.UUID;
import static com.revature.wordsaway.utils.Constants.BOARD_SIZE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.Mockito.*;

public class BoardServiceTest {

    @Test
    public void test_validateMove_RealWordHorizontalMoveOnBlankBoard_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
            BoardRepository mockRepo = mock(BoardRepository.class);
            new BoardService(mockRepo);
            MoveRequest request = mock(MoveRequest.class);
            when(request.getBoardID()).thenReturn(UUID.fromString("00000000-0000-0000-0000-000000000000"));
            Board mockBoard = mock(Board.class);
            when(mockRepo.findBoardByID(any())).thenReturn(mockBoard);
            anagramServiceMockedStatic.when(() -> AnagramService.isWord(any())).thenReturn(true);
            char[] blankLine = new char[BOARD_SIZE];
            Arrays.fill(blankLine, '.');
            when(mockBoard.getRow(intThat(i -> i >= 0 && i < BOARD_SIZE))).thenReturn(blankLine);
            when(mockBoard.getColumn(intThat(i -> i >= 0 && i < BOARD_SIZE))).thenReturn(blankLine);
            char[] move = new char[BOARD_SIZE*BOARD_SIZE];
            Arrays.fill(move, '.');
            move[0] = 'C';
            move[1] = 'A';
            move[2] = 'T';
            when(request.getMove()).thenReturn(move);
            BoardService.validateMove(request.getBoardID(), request.getMove());
        }
    }

    @Test (expected = InvalidRequestException.class)
    public void test_validateMove_NoMoveOnBlankBoard_fail(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
            BoardRepository mockRepo = mock(BoardRepository.class);
            new BoardService(mockRepo);
            MoveRequest request = mock(MoveRequest.class);
            when(request.getBoardID()).thenReturn(UUID.fromString("00000000-0000-0000-0000-000000000000"));
            Board mockBoard = mock(Board.class);
            when(mockRepo.findBoardByID(any())).thenReturn(mockBoard);
            anagramServiceMockedStatic.when(() -> AnagramService.isWord(any())).thenReturn(true);
            char[] blankLine = new char[BOARD_SIZE];
            Arrays.fill(blankLine, '.');
            when(mockBoard.getRow(intThat(i -> i >= 0 && i < BOARD_SIZE))).thenReturn(blankLine);
            when(mockBoard.getColumn(intThat(i -> i >= 0 && i < BOARD_SIZE))).thenReturn(blankLine);
            char[] move = new char[BOARD_SIZE*BOARD_SIZE];
            Arrays.fill(move, '.');
            when(request.getMove()).thenReturn(move);
            BoardService.validateMove(request.getBoardID(), request.getMove());
        }
    }


    @Test
    public void test_validateMove_UnconnectedMoveOnBoardWithOneLetter_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
            BoardRepository mockRepo = mock(BoardRepository.class);
            new BoardService(mockRepo);
            MoveRequest request = mock(MoveRequest.class);
            when(request.getBoardID()).thenReturn(UUID.fromString("00000000-0000-0000-0000-000000000000"));
            Board mockBoard = mock(Board.class);
            when(mockRepo.findBoardByID(any())).thenReturn(mockBoard);
            anagramServiceMockedStatic.when(() -> AnagramService.isWord(any())).thenReturn(true);
            char[] blankLine = new char[BOARD_SIZE];
            Arrays.fill(blankLine, '.');
            char[] lineWithLetter =  new char[BOARD_SIZE];
            Arrays.fill(lineWithLetter, '.');
            lineWithLetter[7] = 'A';
            when(mockBoard.getRow(intThat(i -> i != 7 && i >= 0 && i < BOARD_SIZE))).thenReturn(blankLine);
            when(mockBoard.getColumn(intThat(i -> i != 7 && i >= 0 && i < BOARD_SIZE))).thenReturn(blankLine);
            when(mockBoard.getRow(7)).thenReturn(lineWithLetter);
            when(mockBoard.getColumn(7)).thenReturn(lineWithLetter);
            char[] move = new char[BOARD_SIZE*BOARD_SIZE];
            Arrays.fill(move, '.');
            move[0] = 'C';
            move[1] = 'A';
            move[2] = 'T';
            move[7 * BOARD_SIZE + 7] = 'A';
            when(request.getMove()).thenReturn(move);
            BoardService.validateMove(request.getBoardID(), request.getMove());
        }
    }

    @Test
    public void test_validateMove_ConnectedHorizontalFrontMoveOnBoardWithOneLetter_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
            BoardRepository mockRepo = mock(BoardRepository.class);
            new BoardService(mockRepo);
            MoveRequest request = mock(MoveRequest.class);
            when(request.getBoardID()).thenReturn(UUID.fromString("00000000-0000-0000-0000-000000000000"));
            Board mockBoard = mock(Board.class);
            when(mockRepo.findBoardByID(any())).thenReturn(mockBoard);
            anagramServiceMockedStatic.when(() -> AnagramService.isWord(any())).thenReturn(true);
            char[] blankLine = new char[BOARD_SIZE];
            Arrays.fill(blankLine, '.');
            char[] lineWithLetter =  new char[BOARD_SIZE];
            Arrays.fill(lineWithLetter, '.');
            lineWithLetter[7] = 'A';
            when(mockBoard.getRow(intThat(i -> i != 7 && i >= 0 && i < BOARD_SIZE))).thenReturn(blankLine);
            when(mockBoard.getColumn(intThat(i -> i != 7 && i >= 0 && i < BOARD_SIZE))).thenReturn(blankLine);
            when(mockBoard.getRow(7)).thenReturn(lineWithLetter);
            when(mockBoard.getColumn(7)).thenReturn(lineWithLetter);
            char[] move = new char[BOARD_SIZE*BOARD_SIZE];
            Arrays.fill(move, '.');
            move[7 * BOARD_SIZE + 5] = 'B';
            move[7 * BOARD_SIZE + 6] = 'A';
            move[7 * BOARD_SIZE + 7] = 'A';
            when(request.getMove()).thenReturn(move);
            BoardService.validateMove(request.getBoardID(), request.getMove());
        }
    }

    @Test
    public void test_validateMove_ConnectedHorizontalBackMoveOnBoardWithOneLetter_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
            BoardRepository mockRepo = mock(BoardRepository.class);
            new BoardService(mockRepo);
            MoveRequest request = mock(MoveRequest.class);
            when(request.getBoardID()).thenReturn(UUID.fromString("00000000-0000-0000-0000-000000000000"));
            Board mockBoard = mock(Board.class);
            when(mockRepo.findBoardByID(any())).thenReturn(mockBoard);
            anagramServiceMockedStatic.when(() -> AnagramService.isWord(any())).thenReturn(true);
            char[] blankLine = new char[BOARD_SIZE];
            Arrays.fill(blankLine, '.');
            char[] lineWithLetter =  new char[BOARD_SIZE];
            Arrays.fill(lineWithLetter, '.');
            lineWithLetter[7] = 'A';
            when(mockBoard.getRow(intThat(i -> i != 7 && i >= 0 && i < BOARD_SIZE))).thenReturn(blankLine);
            when(mockBoard.getColumn(intThat(i -> i != 7 && i >= 0 && i < BOARD_SIZE))).thenReturn(blankLine);
            when(mockBoard.getRow(7)).thenReturn(lineWithLetter);
            when(mockBoard.getColumn(7)).thenReturn(lineWithLetter);
            char[] move = new char[BOARD_SIZE*BOARD_SIZE];
            Arrays.fill(move, '.');
            move[7 * BOARD_SIZE + 7] = 'A';
            move[7 * BOARD_SIZE + 8] = 'C';
            move[7 * BOARD_SIZE + 9] = 'T';
            when(request.getMove()).thenReturn(move);
            BoardService.validateMove(request.getBoardID(), request.getMove());
        }
    }
}