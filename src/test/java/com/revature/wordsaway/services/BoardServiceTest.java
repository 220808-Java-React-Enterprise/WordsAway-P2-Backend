package com.revature.wordsaway.services;

import com.revature.wordsaway.dtos.requests.MoveRequest;
import com.revature.wordsaway.models.Board;
import com.revature.wordsaway.repositories.BoardRepository;
import com.revature.wordsaway.utils.customExceptions.InvalidRequestException;
import org.junit.jupiter.api.*;
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
    private MockedStatic<AnagramService> anagramServiceMockedStatic;
    private Board mockBoard;
    private MoveRequest request;
    private char[] move = new char[BOARD_SIZE*BOARD_SIZE];

    private static final char[] BLANK_LINE = {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'};

    @BeforeEach
    public void setupTest(){
        mockRepo = mock(BoardRepository.class);
        boardService = new BoardService(mockRepo);
        anagramServiceMockedStatic = mockStatic(AnagramService.class);
        anagramServiceMockedStatic.when(() -> AnagramService.isWord(any())).thenReturn(true);
        mockBoard = mock(Board.class);
        when(mockRepo.findBoardByID(any())).thenReturn(mockBoard);
        request = mock(MoveRequest.class);
        when(request.getBoardID()).thenReturn(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        Arrays.fill(move, '.');
    }

    @AfterEach
    public void setdownTest(){
        mockRepo = null;
        boardService = null;
        anagramServiceMockedStatic.close();
        anagramServiceMockedStatic = null;
        mockBoard = null;
        request = null;
        Arrays.fill(move, '.');
    }

    private void setupBlankBoard(){
        when(mockBoard.getRow(intThat(i -> i >= 0 && i < BOARD_SIZE))).thenReturn(BLANK_LINE);
        when(mockBoard.getColumn(intThat(i -> i >= 0 && i < BOARD_SIZE))).thenReturn(BLANK_LINE);
    }

    private void setupBoardWithOneLetter(){
        char[] lineWithLetter = BLANK_LINE.clone();
        lineWithLetter[7] = 'A';
        when(mockBoard.getRow(intThat(i -> i != 7 && i >= 0 && i < BOARD_SIZE))).thenReturn(BLANK_LINE);
        when(mockBoard.getColumn(intThat(i -> i != 7 && i >= 0 && i < BOARD_SIZE))).thenReturn(BLANK_LINE);
        when(mockBoard.getRow(7)).thenReturn(lineWithLetter);
        when(mockBoard.getColumn(7)).thenReturn(lineWithLetter);
    }

    private void setupBoardWithThreeLetters(){
        char[] lineWithLetter = BLANK_LINE.clone();
        lineWithLetter[7] = 'C';
        char[] lineWithLetters = BLANK_LINE.clone();
        lineWithLetters[7] = 'A';
        lineWithLetters[11] = 'C';
        when(mockBoard.getRow(intThat(i -> i != 7 && i != 11 && i >= 0 && i < BOARD_SIZE))).thenReturn(BLANK_LINE);
        when(mockBoard.getColumn(intThat(i -> i != 7 && i != 11 && i >= 0 && i < BOARD_SIZE))).thenReturn(BLANK_LINE);
        when(mockBoard.getRow(7)).thenReturn(lineWithLetters);
        when(mockBoard.getColumn(7)).thenReturn(lineWithLetters);
        when(mockBoard.getRow(11)).thenReturn(lineWithLetter);
        when(mockBoard.getColumn(11)).thenReturn(lineWithLetter);
    }

    @RepeatedTest(BOARD_SIZE * BOARD_SIZE)
    public void test_validateMove_LongHorizontalMoveOnBlankBoard_succeed(RepetitionInfo repetitionInfo){
        setupBlankBoard();
        int r = repetitionInfo.getCurrentRepetition() - 1;
        if(r % BOARD_SIZE >= BOARD_SIZE - 2) return; //TODO do math to repeat less rather than returning here
        move[r] = 'C';
        move[r + 1] = 'A';
        move[r + 2] = 'T';
        when(request.getMove()).thenReturn(move);
        boardService.validateMove(request);
        verify(mockRepo, times(1)).findBoardByID(any());
        anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
    }

    @RepeatedTest(BOARD_SIZE * BOARD_SIZE)
    public void test_validateMove_LongVerticalMoveOnBlankBoard_succeed(RepetitionInfo repetitionInfo){
        setupBlankBoard();
        int r = repetitionInfo.getCurrentRepetition() - 1;
        if(r / BOARD_SIZE >= BOARD_SIZE - 2) return; //TODO do math to repeat less rather than returning here
        move[r] = 'C';
        move[r + BOARD_SIZE] = 'A';
        move[r + BOARD_SIZE * 2] = 'T';
        when(request.getMove()).thenReturn(move);
        boardService.validateMove(request);
        verify(mockRepo, times(1)).findBoardByID(any());
        anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
    }

    @RepeatedTest(BOARD_SIZE * BOARD_SIZE)
    public void test_validateMove_ShortMoveOnBlankBoard_succeed(RepetitionInfo repetitionInfo){
        setupBlankBoard();
        int r = repetitionInfo.getCurrentRepetition() - 1;
        move[r] = 'I';
        when(request.getMove()).thenReturn(move);
        boardService.validateMove(request);
        verify(mockRepo, times(1)).findBoardByID(any());
        anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
    }

    @Test
    public void test_validateMove_NoMoveOnBlankBoard_fail(){
        setupBlankBoard();
        when(request.getMove()).thenReturn(move);
        InvalidRequestException thrown = Assertions.assertThrows(InvalidRequestException.class, () -> {
            boardService.validateMove(request);
        });
        verify(mockRepo, times(1)).findBoardByID(any());
        Assertions.assertEquals("Invalid Move. Must be some change in boards.", thrown.getMessage());
    }

    @Test
    public void test_validateMove_UnconnectedLongHorizontalMoveOnBoardWithOneLetter_succeed(){
        //TODO make a repeated test
        setupBoardWithOneLetter();
        move[0] = 'C';
        move[1] = 'A';
        move[2] = 'T';
        move[7 * BOARD_SIZE + 7] = 'A';
        when(request.getMove()).thenReturn(move);
        boardService.validateMove(request);
        verify(mockRepo, times(1)).findBoardByID(any());
        anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
    }

    @Test
    public void test_validateMove_UnconnectedLongVerticalMoveOnBoardWithOneLetter_succeed(){
        //TODO make a repeated test
        setupBoardWithOneLetter();
        move[0] = 'C';
        move[16] = 'A';
        move[32] = 'T';
        move[7 * BOARD_SIZE + 7] = 'A';
        when(request.getMove()).thenReturn(move);
        boardService.validateMove(request);
        verify(mockRepo, times(1)).findBoardByID(any());
        anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
    }

    @Test
    public void test_validateMove_UnconnectedShortMoveOnBoardWithOneLetter_succeed(){
        //TODO make a repeated test
        setupBoardWithOneLetter();
        move[0] = 'I';
        move[7 * BOARD_SIZE + 7] = 'A';
        when(request.getMove()).thenReturn(move);
        boardService.validateMove(request);
        verify(mockRepo, times(1)).findBoardByID(any());
        anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
    }

    @Test
    public void test_validateMove_ConnectedLongHorizontalFrontMoveOnBoardWithOneLetter_succeed(){
        setupBoardWithOneLetter();
        move[7 * BOARD_SIZE + 5] = 'B';
        move[7 * BOARD_SIZE + 6] = 'A';
        move[7 * BOARD_SIZE + 7] = 'A';
        when(request.getMove()).thenReturn(move);
        boardService.validateMove(request);
        verify(mockRepo, times(1)).findBoardByID(any());
        anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
    }

    @Test
    public void test_validateMove_ConnectedShortHorizontalFrontMoveOnBoardWithOneLetter_succeed(){
        setupBoardWithOneLetter();
        move[7 * BOARD_SIZE + 6] = 'P';
        move[7 * BOARD_SIZE + 7] = 'A';
        when(request.getMove()).thenReturn(move);
        boardService.validateMove(request);
        verify(mockRepo, times(1)).findBoardByID(any());
        anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
    }

    @Test
    public void test_validateMove_ConnectedLongVerticalFrontMoveOnBoardWithOneLetter_succeed(){
        setupBoardWithOneLetter();
        move[5 * BOARD_SIZE + 7] = 'B';
        move[6 * BOARD_SIZE + 7] = 'A';
        move[7 * BOARD_SIZE + 7] = 'A';
        when(request.getMove()).thenReturn(move);
        boardService.validateMove(request);
        verify(mockRepo, times(1)).findBoardByID(any());
        anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
    }

    @Test
    public void test_validateMove_ConnectedShortVerticalFrontMoveOnBoardWithOneLetter_succeed(){
        setupBoardWithOneLetter();
        move[6 * BOARD_SIZE + 7] = 'P';
        move[7 * BOARD_SIZE + 7] = 'A';
        when(request.getMove()).thenReturn(move);
        boardService.validateMove(request);
        verify(mockRepo, times(1)).findBoardByID(any());
        anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
    }

    @Test
    public void test_validateMove_ConnectedLongHorizontalBackMoveOnBoardWithOneLetter_succeed(){
        setupBoardWithOneLetter();
        move[7 * BOARD_SIZE + 7] = 'A';
        move[7 * BOARD_SIZE + 8] = 'C';
        move[7 * BOARD_SIZE + 9] = 'T';
        when(request.getMove()).thenReturn(move);
        boardService.validateMove(request);
        verify(mockRepo, times(1)).findBoardByID(any());
        anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
    }

    @Test
    public void test_validateMove_ConnectedShortHorizontalBackMoveOnBoardWithOneLetter_succeed(){
        setupBoardWithOneLetter();
        move[7 * BOARD_SIZE + 7] = 'A';
        move[7 * BOARD_SIZE + 8] = 'T';
        when(request.getMove()).thenReturn(move);
        boardService.validateMove(request);
        verify(mockRepo, times(1)).findBoardByID(any());
        anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
    }

    @Test
    public void test_validateMove_ConnectedLongVerticalBackMoveOnBoardWithOneLetter_succeed(){
        setupBoardWithOneLetter();
        move[7 * BOARD_SIZE + 7] = 'A';
        move[8 * BOARD_SIZE + 7] = 'C';
        move[9 * BOARD_SIZE + 7] = 'T';
        when(request.getMove()).thenReturn(move);
        boardService.validateMove(request);
        verify(mockRepo, times(1)).findBoardByID(any());
        anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
    }

    @Test
    public void test_validateMove_ConnectedShortVerticalBackMoveOnBoardWithOneLetter_succeed(){
        setupBoardWithOneLetter();
        move[7 * BOARD_SIZE + 7] = 'A';
        move[8 * BOARD_SIZE + 7] = 'T';
        when(request.getMove()).thenReturn(move);
        boardService.validateMove(request);
        verify(mockRepo, times(1)).findBoardByID(any());
        anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
    }

    @Test
    public void test_validateMove_ConnectedLongHorizontalAroundMoveOnBoardWithOneLetter_succeed(){
        setupBoardWithOneLetter();
        move[7 * BOARD_SIZE + 6] = 'C';
        move[7 * BOARD_SIZE + 7] = 'A';
        move[7 * BOARD_SIZE + 8] = 'T';
        when(request.getMove()).thenReturn(move);
        boardService.validateMove(request);
        verify(mockRepo, times(1)).findBoardByID(any());
        anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
    }

    @Test
    public void test_validateMove_ConnectedLongVerticalAroundMoveOnBoardWithOneLetter_succeed(){
        setupBoardWithOneLetter();
        move[6 * BOARD_SIZE + 7] = 'C';
        move[7 * BOARD_SIZE + 7] = 'A';
        move[8 * BOARD_SIZE + 7] = 'T';
        when(request.getMove()).thenReturn(move);
        boardService.validateMove(request);
        verify(mockRepo, times(1)).findBoardByID(any());
        anagramServiceMockedStatic.verify(() -> AnagramService.isWord(any()), times(1));
    }

    @Test
    public void test_validateMove_ConnectedLongHorizontalBetweenMoveOnBoardWithThreeLetters_succeed(){
        setupBoardWithThreeLetters();
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

    @Test
    public void test_validateMove_ConnectedLongVerticalBetweenMoveOnBoardWithThreeLetters_succeed(){
        setupBoardWithThreeLetters();
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

    @Test
    public void test_validateMove_ConnectedAsteriskHorizontalFrontMoveOnBoardWithOneLetter_succeed(){
        setupBoardWithOneLetter();
        move[7 * BOARD_SIZE + 6] = '*';
        move[7 * BOARD_SIZE + 7] = 'A';
        when(request.getMove()).thenReturn(move);
        boardService.validateMove(request);
        verify(mockRepo, times(1)).findBoardByID(any());
    }

    @Test
    public void test_validateMove_ConnectedAsteriskHorizontalBackMoveOnBoardWithOneLetter_succeed(){
        setupBoardWithOneLetter();
        move[7 * BOARD_SIZE + 7] = 'A';
        move[7 * BOARD_SIZE + 8] = '*';
        when(request.getMove()).thenReturn(move);
        boardService.validateMove(request);
        verify(mockRepo, times(1)).findBoardByID(any());
    }

    @Test
    public void test_validateMove_ConnectedAsteriskVerticalFrontMoveOnBoardWithOneLetter_succeed(){
        setupBoardWithOneLetter();
        move[6 * BOARD_SIZE + 7] = '*';
        move[7 * BOARD_SIZE + 7] = 'A';
        when(request.getMove()).thenReturn(move);
        boardService.validateMove(request);
        verify(mockRepo, times(1)).findBoardByID(any());
    }

    @Test
    public void test_validateMove_ConnectedAsteriskVerticalBackMoveOnBoardWithOneLetter_succeed(){
        setupBoardWithOneLetter();
        move[7 * BOARD_SIZE + 7] = 'A';
        move[8 * BOARD_SIZE + 7] = '*';
        when(request.getMove()).thenReturn(move);
        boardService.validateMove(request);
        verify(mockRepo, times(1)).findBoardByID(any());
    }

    @Test
    public void test_validateMove_TwoUnconnectedAsteriskSameRowMoveOnBoardWithOneLetter_fail(){
        setupBoardWithOneLetter();
        move[6 * BOARD_SIZE + 0] = '*';
        move[6 * BOARD_SIZE + 1] = '*';
        move[7 * BOARD_SIZE + 7] = 'A';
        when(request.getMove()).thenReturn(move);
        InvalidRequestException thrown = Assertions.assertThrows(InvalidRequestException.class, () -> {
            boardService.validateMove(request);
        });
        verify(mockRepo, times(1)).findBoardByID(any());
        Assertions.assertEquals("Invalid Move. Illegal characters placed on board.", thrown.getMessage());
    }

    @Test
    public void test_validateMove_TwoUnconnectedAsteriskSameColumnMoveOnBoardWithOneLetter_fail(){
        setupBoardWithOneLetter();
        move[5 * BOARD_SIZE + 0] = '*';
        move[6 * BOARD_SIZE + 0] = '*';
        move[7 * BOARD_SIZE + 7] = 'A';
        when(request.getMove()).thenReturn(move);
        InvalidRequestException thrown = Assertions.assertThrows(InvalidRequestException.class, () -> {
            boardService.validateMove(request);
        });
        verify(mockRepo, times(1)).findBoardByID(any());
        Assertions.assertEquals("Invalid Move. Illegal characters placed on board.", thrown.getMessage());
    }

    @Test
    public void test_validateMove_TwoUnconnectedAsteriskMoveOnBoardWithOneLetter_fail(){
        setupBoardWithOneLetter();
        move[5 * BOARD_SIZE + 0] = '*';
        move[6 * BOARD_SIZE + 1] = '*';
        move[7 * BOARD_SIZE + 7] = 'A';
        when(request.getMove()).thenReturn(move);
        InvalidRequestException thrown = Assertions.assertThrows(InvalidRequestException.class, () -> {
            boardService.validateMove(request);
        });
        verify(mockRepo, times(1)).findBoardByID(any());
        Assertions.assertEquals("Invalid Move. More than one tile in different rows and columns may not be placed at once.",
                thrown.getMessage());
    }

    @Test
    public void test_validateMove_TwoUnconnectedLetterMoveOnBoardWithOneLetter_fail(){
        setupBoardWithOneLetter();
        move[5 * BOARD_SIZE + 0] = 'I';
        move[6 * BOARD_SIZE + 1] = 'A';
        move[7 * BOARD_SIZE + 7] = 'A';
        when(request.getMove()).thenReturn(move);
        InvalidRequestException thrown = Assertions.assertThrows(InvalidRequestException.class, () -> {
            boardService.validateMove(request);
        });
        verify(mockRepo, times(1)).findBoardByID(any());
        Assertions.assertEquals("Invalid Move. More than one tile in different rows and columns may not be placed at once.",
                thrown.getMessage());
    }

    @Test
    public void test_validateMove_TwoConnectedAsteriskHorizontalFrontMoveOnBoardWithOneLetter_fail(){
        setupBoardWithOneLetter();
        move[7 * BOARD_SIZE + 5] = '*';
        move[7 * BOARD_SIZE + 6] = '*';
        move[7 * BOARD_SIZE + 7] = 'A';
        when(request.getMove()).thenReturn(move);
        InvalidRequestException thrown = Assertions.assertThrows(InvalidRequestException.class, () -> {
            boardService.validateMove(request);
        });
        verify(mockRepo, times(1)).findBoardByID(any());
        Assertions.assertEquals("Invalid Move. Illegal characters placed on board.", thrown.getMessage());
    }

    @Test
    public void test_validateMove_TwoConnectedAsteriskHorizontalBackMoveOnBoardWithOneLetter_fail(){
        setupBoardWithOneLetter();
        move[7 * BOARD_SIZE + 7] = 'A';
        move[7 * BOARD_SIZE + 8] = '*';
        move[7 * BOARD_SIZE + 9] = '*';
        when(request.getMove()).thenReturn(move);
        InvalidRequestException thrown = Assertions.assertThrows(InvalidRequestException.class, () -> {
            boardService.validateMove(request);
        });
        verify(mockRepo, times(1)).findBoardByID(any());
        Assertions.assertEquals("Invalid Move. Illegal characters placed on board.", thrown.getMessage());
    }

    @Test
    public void test_validateMove_TwoConnectedAsteriskVerticalFrontMoveOnBoardWithOneLetter_fail(){
        setupBoardWithOneLetter();
        move[5 * BOARD_SIZE + 7] = '*';
        move[6 * BOARD_SIZE + 7] = '*';
        move[7 * BOARD_SIZE + 7] = 'A';
        when(request.getMove()).thenReturn(move);
        InvalidRequestException thrown = Assertions.assertThrows(InvalidRequestException.class, () -> {
            boardService.validateMove(request);
        });
        verify(mockRepo, times(1)).findBoardByID(any());
        Assertions.assertEquals("Invalid Move. Illegal characters placed on board.", thrown.getMessage());
    }

    @Test
    public void test_validateMove_TwoConnectedAsteriskVerticalBackMoveOnBoardWithOneLetter_fail(){
        setupBoardWithOneLetter();
        move[7 * BOARD_SIZE + 7] = 'A';
        move[8 * BOARD_SIZE + 7] = '*';
        move[9 * BOARD_SIZE + 7] = '*';
        when(request.getMove()).thenReturn(move);
        InvalidRequestException thrown = Assertions.assertThrows(InvalidRequestException.class, () -> {
            boardService.validateMove(request);
        });
        verify(mockRepo, times(1)).findBoardByID(any());
        Assertions.assertEquals("Invalid Move. Illegal characters placed on board.", thrown.getMessage());
    }

    @Test
    public void test_validateMove_TwoConnectedAsteriskHorizontalAroundMoveOnBoardWithOneLetter_fail(){
        setupBoardWithOneLetter();
        move[7 * BOARD_SIZE + 6] = '*';
        move[7 * BOARD_SIZE + 7] = 'A';
        move[7 * BOARD_SIZE + 8] = '*';
        when(request.getMove()).thenReturn(move);
        InvalidRequestException thrown = Assertions.assertThrows(InvalidRequestException.class, () -> {
            boardService.validateMove(request);
        });
        verify(mockRepo, times(1)).findBoardByID(any());
        Assertions.assertEquals("Invalid Move. Illegal characters placed on board.", thrown.getMessage());
    }

    @Test
    public void test_validateMove_TwoConnectedAsteriskVerticalAroundMoveOnBoardWithOneLetter_fail(){
        setupBoardWithOneLetter();
        move[6 * BOARD_SIZE + 7] = '*';
        move[7 * BOARD_SIZE + 7] = 'A';
        move[8 * BOARD_SIZE + 7] = '*';
        when(request.getMove()).thenReturn(move);
        InvalidRequestException thrown = Assertions.assertThrows(InvalidRequestException.class, () -> {
            boardService.validateMove(request);
        });
        verify(mockRepo, times(1)).findBoardByID(any());
        Assertions.assertEquals("Invalid Move. Illegal characters placed on board.", thrown.getMessage());
    }
}