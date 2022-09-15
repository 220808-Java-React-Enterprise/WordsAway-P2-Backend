package com.revature.wordsaway.services;

import com.revature.wordsaway.dtos.requests.MoveRequest;
import com.revature.wordsaway.models.Board;
import com.revature.wordsaway.repositories.BoardRepository;
import com.revature.wordsaway.utils.customExceptions.InvalidRequestException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import java.util.Arrays;
import java.util.UUID;
import static com.revature.wordsaway.utils.Constants.BOARD_SIZE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.Mockito.*;

public class BoardServiceTest {
    private BoardRepository mockRepo;
    private BoardService boardService;

    @Before
    public void setup(){
        mockRepo = mock(BoardRepository.class);
        boardService = new BoardService(mockRepo);
    }


    @Test
    public void test_validateMove_LongHorizontalMoveOnBlankBoard_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
            anagramServiceMockedStatic.when(() -> AnagramService.isWord(any())).thenReturn(true);
            Board mockBoard = mock(Board.class);
            when(mockRepo.findBoardByID(any())).thenReturn(mockBoard);
            char[] blankLine = new char[BOARD_SIZE];
            Arrays.fill(blankLine, '.');
            when(mockBoard.getRow(intThat(i -> i >= 0 && i < BOARD_SIZE))).thenReturn(blankLine);
            when(mockBoard.getColumn(intThat(i -> i >= 0 && i < BOARD_SIZE))).thenReturn(blankLine);
            MoveRequest request = mock(MoveRequest.class);
            char[] move = new char[BOARD_SIZE*BOARD_SIZE];
            Arrays.fill(move, '.');
            move[0] = 'C';
            move[1] = 'A';
            move[2] = 'T';
            when(request.getMove()).thenReturn(move);
            when(request.getBoardID()).thenReturn(UUID.fromString("00000000-0000-0000-0000-000000000000"));
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
            anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
        }
    }

    @Test
    public void test_validateMove_LongVerticalMoveOnBlankBoard_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[16] = 'A';
            move[32] = 'T';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
            anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
        }
    }

    @Test
    public void test_validateMove_ShortMoveOnBlankBoard_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[0] = 'I';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
            anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
        }
    }

    @Test (expected = InvalidRequestException.class)
    public void test_validateMove_NoMoveOnBlankBoard_fail(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){

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
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
            anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
        }
    }


    @Test
    public void test_validateMove_UnconnectedLongHorizontalMoveOnBoardWithOneLetter_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
            anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
        }
    }

    @Test
    public void test_validateMove_UnconnectedLongVerticalMoveOnBoardWithOneLetter_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[16] = 'A';
            move[32] = 'T';
            move[7 * BOARD_SIZE + 7] = 'A';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
            anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
        }
    }

    @Test
    public void test_validateMove_UnconnectedShortMoveOnBoardWithOneLetter_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[0] = 'I';
            move[7 * BOARD_SIZE + 7] = 'A';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
            anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
        }
    }

    @Test
    public void test_validateMove_ConnectedLongHorizontalFrontMoveOnBoardWithOneLetter_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
            anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
        }
    }

    @Test
    public void test_validateMove_ConnectedShortHorizontalFrontMoveOnBoardWithOneLetter_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[7 * BOARD_SIZE + 6] = 'P';
            move[7 * BOARD_SIZE + 7] = 'A';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
            anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
        }
    }

    @Test
    public void test_validateMove_ConnectedLongVerticalFrontMoveOnBoardWithOneLetter_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[5 * BOARD_SIZE + 7] = 'B';
            move[6 * BOARD_SIZE + 7] = 'A';
            move[7 * BOARD_SIZE + 7] = 'A';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
            anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
        }
    }

    @Test
    public void test_validateMove_ConnectedShortVerticalFrontMoveOnBoardWithOneLetter_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[6 * BOARD_SIZE + 7] = 'P';
            move[7 * BOARD_SIZE + 7] = 'A';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
            anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
        }
    }

    @Test
    public void test_validateMove_ConnectedLongHorizontalBackMoveOnBoardWithOneLetter_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
            anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
        }
    }

    @Test
    public void test_validateMove_ConnectedShortHorizontalBackMoveOnBoardWithOneLetter_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[7 * BOARD_SIZE + 8] = 'T';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
            anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
        }
    }

    @Test
    public void test_validateMove_ConnectedLongVerticalBackMoveOnBoardWithOneLetter_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[8 * BOARD_SIZE + 7] = 'C';
            move[9 * BOARD_SIZE + 7] = 'T';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
            anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
        }
    }

    @Test
    public void test_validateMove_ConnectedShortVerticalBackMoveOnBoardWithOneLetter_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[8 * BOARD_SIZE + 7] = 'T';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
            anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
        }
    }

    @Test
    public void test_validateMove_ConnectedLongHorizontalAroundMoveOnBoardWithOneLetter_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[7 * BOARD_SIZE + 6] = 'C';
            move[7 * BOARD_SIZE + 7] = 'A';
            move[7 * BOARD_SIZE + 8] = 'T';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
            anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
        }
    }

    @Test
    public void test_validateMove_ConnectedLongVerticalAroundMoveOnBoardWithOneLetter_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[6 * BOARD_SIZE + 7] = 'C';
            move[7 * BOARD_SIZE + 7] = 'A';
            move[8 * BOARD_SIZE + 7] = 'T';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
            anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
        }
    }

    @Test
    public void test_validateMove_ConnectedLongHorizontalBetweenMoveOnBoardWithThreeLetter_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
            MoveRequest request = mock(MoveRequest.class);
            when(request.getBoardID()).thenReturn(UUID.fromString("00000000-0000-0000-0000-000000000000"));
            Board mockBoard = mock(Board.class);
            when(mockRepo.findBoardByID(any())).thenReturn(mockBoard);
            anagramServiceMockedStatic.when(() -> AnagramService.isWord(any())).thenReturn(true);
            char[] blankLine = new char[BOARD_SIZE];
            Arrays.fill(blankLine, '.');
            char[] lineWithLetters =  new char[BOARD_SIZE];
            Arrays.fill(lineWithLetters, '.');
            lineWithLetters[7] = 'A';
            lineWithLetters[11] = 'C';
            char[] lineWithLetter =  new char[BOARD_SIZE];
            Arrays.fill(lineWithLetter, '.');
            lineWithLetter[7] = 'C';
            when(mockBoard.getRow(intThat(i -> i != 7 && i != 11 && i >= 0 && i < BOARD_SIZE))).thenReturn(blankLine);
            when(mockBoard.getColumn(intThat(i -> i != 7 && i != 11 && i >= 0 && i < BOARD_SIZE))).thenReturn(blankLine);
            when(mockBoard.getRow(7)).thenReturn(lineWithLetters);
            when(mockBoard.getColumn(7)).thenReturn(lineWithLetters);
            when(mockBoard.getRow(11)).thenReturn(lineWithLetter);
            when(mockBoard.getColumn(11)).thenReturn(lineWithLetter);
            char[] move = new char[BOARD_SIZE*BOARD_SIZE];
            Arrays.fill(move, '.');
            move[7 * BOARD_SIZE + 7] = 'A';
            move[7 * BOARD_SIZE + 8] = 'T';
            move[7 * BOARD_SIZE + 9] = 'T';
            move[7 * BOARD_SIZE + 10] = 'I';
            move[7 * BOARD_SIZE + 11] = 'C';
            move[11 * BOARD_SIZE + 7] = 'C';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
            anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
        }
    }

    @Test
    public void test_validateMove_ConnectedLongVerticalBetweenMoveOnBoardWithThreeLetter_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
            MoveRequest request = mock(MoveRequest.class);
            when(request.getBoardID()).thenReturn(UUID.fromString("00000000-0000-0000-0000-000000000000"));
            Board mockBoard = mock(Board.class);
            when(mockRepo.findBoardByID(any())).thenReturn(mockBoard);
            anagramServiceMockedStatic.when(() -> AnagramService.isWord(any())).thenReturn(true);
            char[] blankLine = new char[BOARD_SIZE];
            Arrays.fill(blankLine, '.');
            char[] lineWithLetters =  new char[BOARD_SIZE];
            Arrays.fill(lineWithLetters, '.');
            lineWithLetters[7] = 'A';
            lineWithLetters[11] = 'C';
            char[] lineWithLetter =  new char[BOARD_SIZE];
            Arrays.fill(lineWithLetter, '.');
            lineWithLetter[7] = 'C';
            when(mockBoard.getRow(intThat(i -> i != 7 && i != 11 && i >= 0 && i < BOARD_SIZE))).thenReturn(blankLine);
            when(mockBoard.getColumn(intThat(i -> i != 7 && i != 11 && i >= 0 && i < BOARD_SIZE))).thenReturn(blankLine);
            when(mockBoard.getRow(7)).thenReturn(lineWithLetters);
            when(mockBoard.getColumn(7)).thenReturn(lineWithLetters);
            when(mockBoard.getRow(11)).thenReturn(lineWithLetter);
            when(mockBoard.getColumn(11)).thenReturn(lineWithLetter);
            char[] move = new char[BOARD_SIZE*BOARD_SIZE];
            Arrays.fill(move, '.');
            move[7 * BOARD_SIZE + 7] = 'A';
            move[8 * BOARD_SIZE + 7] = 'T';
            move[9 * BOARD_SIZE + 7] = 'T';
            move[10 * BOARD_SIZE + 7] = 'I';
            move[11 * BOARD_SIZE + 7] = 'C';
            move[7 * BOARD_SIZE + 11] = 'C';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
            anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
        }
    }

    @Test
    public void test_validateMove_ConnectedAsteriskHorizontalFrontMoveOnBoardWithOneLetter_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[7 * BOARD_SIZE + 6] = '*';
            move[7 * BOARD_SIZE + 7] = 'A';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
        }
    }

    @Test
    public void test_validateMove_ConnectedAsteriskHorizontalBackMoveOnBoardWithOneLetter_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[7 * BOARD_SIZE + 8] = '*';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
        }
    }

    @Test
    public void test_validateMove_ConnectedAsteriskVerticalFrontMoveOnBoardWithOneLetter_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[6 * BOARD_SIZE + 7] = '*';
            move[7 * BOARD_SIZE + 7] = 'A';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
        }
    }

    @Test
    public void test_validateMove_ConnectedAsteriskVerticalBackMoveOnBoardWithOneLetter_succeed(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[8 * BOARD_SIZE + 7] = '*';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
        }
    }

    @Test(expected = InvalidRequestException.class)
    public void test_validateMove_TwoUnconnectedAsteriskSameRowMoveOnBoardWithOneLetter_fail(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[6 * BOARD_SIZE + 0] = '*';
            move[6 * BOARD_SIZE + 1] = '*';
            move[7 * BOARD_SIZE + 7] = 'A';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
        }
    }

    @Test(expected = InvalidRequestException.class)
    public void test_validateMove_TwoUnconnectedAsteriskSameColumnMoveOnBoardWithOneLetter_fail(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[5 * BOARD_SIZE + 0] = '*';
            move[6 * BOARD_SIZE + 0] = '*';
            move[7 * BOARD_SIZE + 7] = 'A';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
        }
    }

    @Test(expected = InvalidRequestException.class)
    public void test_validateMove_TwoUnconnectedAsteriskMoveOnBoardWithOneLetter_fail(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[5 * BOARD_SIZE + 0] = '*';
            move[6 * BOARD_SIZE + 1] = '*';
            move[7 * BOARD_SIZE + 7] = 'A';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
        }
    }

    @Test(expected = InvalidRequestException.class)
    public void test_validateMove_TwoUnconnectedLetterMoveOnBoardWithOneLetter_fail(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[5 * BOARD_SIZE + 0] = 'I';
            move[6 * BOARD_SIZE + 1] = 'A';
            move[7 * BOARD_SIZE + 7] = 'A';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
        }
    }

    @Test(expected = InvalidRequestException.class)
    public void test_validateMove_TwoConnectedAsteriskHorizontalFrontMoveOnBoardWithOneLetter_fail(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[7 * BOARD_SIZE + 5] = '*';
            move[7 * BOARD_SIZE + 6] = '*';
            move[7 * BOARD_SIZE + 7] = 'A';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
        }
    }

    @Test(expected = InvalidRequestException.class)
    public void test_validateMove_TwoConnectedAsteriskHorizontalBackMoveOnBoardWithOneLetter_fail(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[7 * BOARD_SIZE + 8] = '*';
            move[7 * BOARD_SIZE + 9] = '*';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
        }
    }

    @Test(expected = InvalidRequestException.class)
    public void test_validateMove_TwoConnectedAsteriskVerticalFrontMoveOnBoardWithOneLetter_fail(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[5 * BOARD_SIZE + 7] = '*';
            move[6 * BOARD_SIZE + 7] = '*';
            move[7 * BOARD_SIZE + 7] = 'A';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
        }
    }

    @Test(expected = InvalidRequestException.class)
    public void test_validateMove_TwoConnectedAsteriskVerticalBackMoveOnBoardWithOneLetter_fail(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[8 * BOARD_SIZE + 7] = '*';
            move[9 * BOARD_SIZE + 7] = '*';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
        }
    }

    @Test(expected = InvalidRequestException.class)
    public void test_validateMove_TwoConnectedAsteriskHorizontalAroundMoveOnBoardWithOneLetter_fail(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[7 * BOARD_SIZE + 6] = '*';
            move[7 * BOARD_SIZE + 7] = 'A';
            move[7 * BOARD_SIZE + 8] = '*';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
        }
    }

    @Test(expected = InvalidRequestException.class)
    public void test_validateMove_TwoConnectedAsteriskVerticalAroundMoveOnBoardWithOneLetter_fail(){
        try(MockedStatic<AnagramService> anagramServiceMockedStatic = mockStatic(AnagramService.class)){
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
            move[6 * BOARD_SIZE + 7] = '*';
            move[7 * BOARD_SIZE + 7] = 'A';
            move[8 * BOARD_SIZE + 7] = '*';
            when(request.getMove()).thenReturn(move);
            boardService.validateMove(request);
            verify(mockRepo, times(1)).findBoardByID(any());
        }
    }
}