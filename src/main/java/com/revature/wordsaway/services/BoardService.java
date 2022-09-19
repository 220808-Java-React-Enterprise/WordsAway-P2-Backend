package com.revature.wordsaway.services;

import com.revature.wordsaway.dtos.requests.MoveRequest;
import com.revature.wordsaway.models.Board;
import com.revature.wordsaway.models.User;
import com.revature.wordsaway.repositories.BoardRepository;
import com.revature.wordsaway.utils.customExceptions.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

import static com.revature.wordsaway.utils.Constants.BOARD_SIZE;

@Service
public class BoardService {
    private static BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }

    public static Board register(User user, UUID gameID, boolean isActive){
        //TODO probably validate some things
        char[] blankArr = new char[BOARD_SIZE*BOARD_SIZE];
        char[] tray = new char[7];

        for (int i = 0; i < tray.length; i++)
            tray[i] = getRandomChar();

        Arrays.fill(blankArr, '.');
        Board board = new Board(
                UUID.randomUUID(),
                user,
                tray,
                0,
                blankArr,
                blankArr,
                gameID,
                isActive
        );
        boardRepository.save(board);
        return board;
    }

    public static void update(Board board){
        boardRepository.updateBoard(board.getId(), board.getFireballs(), board.isActive(), board.getLetters(), board.getTray(), board.getWorms());
    }

    public static Board getByID(UUID boardID) {
        Board board = boardRepository.findBoardByID(boardID);
        if(board == null) throw new InvalidRequestException("No board with ID " + boardID + " found.");
        return board;
    }

    public static List<Board> getByGameID(UUID gameID) {
        List<Board> boards = boardRepository.findBoardByGameID(gameID);
        if(boards == null) throw new InvalidRequestException("No boards with gameID " + gameID + " found.");
        return boards;
    }

    public static Board getOpposingBoard(Board board) {
        Board opposingBoard =  boardRepository.findOpposingBoardByIDAndGameID(board.getId(), board.getGameID());
        if(opposingBoard == null) throw new InvalidRequestException("No boards opposing " + board.getGameID() + " found.");
        return opposingBoard;
    }

    //TODO probably delete this at the end
    public static void deleteAll(){
        boardRepository.deleteAll();
    }

    public static void validateMove(MoveRequest request) throws InvalidRequestException {
        //TODO try to simplify this method.
        Board oldBoard = getByID(request.getBoardID());
        Board newBoard = new Board(oldBoard, request.getMove());
        Set<Integer> oneDiffs = new HashSet<>();
        List<char[]> oneDiffsChanges = new ArrayList<>();
        char[] multipleLetterDifference = null;
        int differences, startChange, endChange;
        for(int i = 0; i < BOARD_SIZE; i++){
            differences = 0; startChange = -1; endChange = BOARD_SIZE;
            for(int j = 0; j < BOARD_SIZE; j++){
                if(oldBoard.getRow(i)[j] != newBoard.getRow(i)[j]) {
                    if(endChange != BOARD_SIZE) throw new InvalidRequestException("Invalid Move. Can not place more than one word at a time.");
                    if(oldBoard.getRow(i)[j] != '.') throw new InvalidRequestException("Invalid Move. Can not place tile on non-empty spot.");
                    if(startChange == -1) startChange = j;
                    differences++;
                }else if(startChange != -1 && endChange == BOARD_SIZE && newBoard.getRow(i)[j] == '.') endChange = j;
            }
            if(differences == 1) oneDiffs.add(i * BOARD_SIZE + startChange);
            if(startChange != -1){
                for(int j = startChange; j >= 0; j--){
                    if(newBoard.getRow(i)[j] != '.') startChange = j;
                    else break;
                }
                for(int j = endChange; j <= BOARD_SIZE; j++){
                    if(newBoard.getRow(i)[j - 1] != '.') endChange = j;
                    else break;
                }
            }
            if(differences == 1 && endChange - startChange > 1) {
                char[] diffChanges = new char[endChange - startChange];
                for(int j = 0; j < diffChanges.length; j++){
                    diffChanges[j] = newBoard.getRow(i)[startChange + j];
                }
                oneDiffsChanges.add(diffChanges);
            }else if (differences > 1) {
                if(multipleLetterDifference != null) throw new InvalidRequestException("Invalid Move. More than one word may not be placed at once.");
                multipleLetterDifference = new char[endChange - startChange];
                for(int j = 0; j < multipleLetterDifference.length; j++){
                    multipleLetterDifference[j] = newBoard.getRow(i)[startChange + j];
                }
            }
            differences = 0; startChange = -1; endChange = BOARD_SIZE;
            for(int j = 0; j < BOARD_SIZE; j++){
                if(oldBoard.getColumn(i)[j] != newBoard.getColumn(i)[j]) {
                    if(endChange != BOARD_SIZE) throw new InvalidRequestException("Invalid Move. Can not place more than one word at a time.");
                    if(oldBoard.getColumn(i)[j] != '.') throw new InvalidRequestException("Invalid Move. Can not place tile on non-empty spot.");
                    if(startChange == -1) startChange = j;
                    differences++;
                }else if(startChange != -1 && endChange == BOARD_SIZE && newBoard.getColumn(i)[j] == '.') endChange = j;
            }
            if(differences == 1) oneDiffs.add(i + startChange * BOARD_SIZE);
            if(startChange != -1) {
                for(int j = startChange; j >= 0; j--){
                    if(newBoard.getColumn(i)[j] != '.')startChange = j;
                    else break;
                }
                for (int j = endChange; j <= BOARD_SIZE; j++) {
                    if (newBoard.getColumn(i)[j - 1] != '.') endChange = j;
                    else break;
                }
            }
            if(differences == 1 && endChange - startChange > 1) {
                char[] diffChanges = new char[endChange - startChange];
                for(int j = 0; j < diffChanges.length; j++){
                    diffChanges[j] = newBoard.getColumn(i)[startChange + j];
                }
                oneDiffsChanges.add(diffChanges);
            }else if (differences > 1) {
                if(multipleLetterDifference != null) throw new InvalidRequestException("Invalid Move. More than one column may not be placed at once.");
                multipleLetterDifference = new char[endChange - startChange];
                for(int j = 0; j < multipleLetterDifference.length; j++){
                    multipleLetterDifference[j] = newBoard.getColumn(i)[startChange + j];
                }
            }
        }
        if(multipleLetterDifference != null) {
            if (!isWord(multipleLetterDifference)) throw new InvalidRequestException("Invalid Move. Placed tiles do not form valid word.");
            else return;
        }else if(oneDiffs.size() > 1) throw new InvalidRequestException("Invalid Move. More than one tile in different rows and columns may not be placed at once.");
        else if (oneDiffs.size() > 0){
            int index = (int) oneDiffs.toArray()[0];
            char c = newBoard.getRow(index / BOARD_SIZE)[index % BOARD_SIZE];
            if(c == '*') return;
            if(oneDiffsChanges.size() > 0) {
                for (char[] change : oneDiffsChanges) {
                    if (!isWord(change))
                        throw new InvalidRequestException("Invalid Move. Placed tiles do not form valid word.");
                }
            }else if (!isWord(new char[] {c}))
                throw new InvalidRequestException("Invalid Move. Placed tiles do not form valid word.");
        } else throw new InvalidRequestException("Invalid Move. Must be some change in boards.");
    }

    public static void validateMove2TheWrathOfKhan(MoveRequest request) throws InvalidRequestException {
        char[] oldLetters = getByID(request.getBoardID()).getLetters();
        char[] newLetters = request.getMove();
        Set<ChangeSpot> changeSpots = new HashSet<>();
        for(int i = 0; i < oldLetters.length; i++){
            if(oldLetters[i] != newLetters[i]){
                changeSpots.add(new ChangeSpot(i));
            }
        }
        if(changeSpots.size() == 0) throw new InvalidRequestException("Invalid Move. Must be some change in boards.");
        ChangeSpot[] changeSpotArr = (ChangeSpot[]) changeSpots.toArray();
        if(changeSpots.size() == 1){
            char c = newLetters[changeSpotArr[0].getI()];
            if(c == '*') return;
            else if (!isWord(new char[] {c}))
                throw new InvalidRequestException("Invalid Move. Placed tiles do not form valid word.");
        }
        boolean checkRow = changeSpotArr[0].row == changeSpotArr[1].row;
        boolean checkColumn = changeSpotArr[0].column == changeSpotArr[1].column;
        if(!checkRow && !checkColumn)
            throw new InvalidRequestException("Invalid Move. All tiles must be placed in either the same row or same column.");
        for(int i = 2; i < changeSpotArr.length; i++){
            if(checkRow && changeSpotArr[i].row != changeSpotArr[0].row)
                throw new InvalidRequestException("Invalid Move. All tiles must be placed in either the same row or same column.");
            if(checkColumn && changeSpotArr[i].column != changeSpotArr[0].column)
                throw new InvalidRequestException("Invalid Move. All tiles must be placed in either the same row or same column.");
        }

        if(!isWord(findConnectedWord(newLetters, changeSpotArr[0], checkRow, checkColumn)))
            throw new InvalidRequestException("Invalid Move. Placed tiles do not form valid word.");
        for(ChangeSpot spot : changeSpotArr){
            if(!isWord(findConnectedWord(newLetters, spot, !checkRow, !checkColumn)))
                throw new InvalidRequestException("Invalid Move. Placed tiles do not form valid word.");
        }
    }

    private static class ChangeSpot{
        int row;
        int column;
        ChangeSpot(int i){
            this.row = i / BOARD_SIZE;
            this.column = i % BOARD_SIZE;
        }
        int getI(){
            return row * BOARD_SIZE + column;
        }
    }

    private static char[] findConnectedWord(char[] letters, ChangeSpot spot, boolean checkRow, boolean checkColumn){
        if(checkRow && checkColumn) throw new IllegalArgumentException("Both checkRow and checkColumn may not be true.");
        if(checkRow){
            int start = spot.row;
            int end = spot.row;
            for(int i = start - 1; i % BOARD_SIZE != BOARD_SIZE - 1 && letters[i] != '.' && letters[i] != '*'; i--){
                start = i;
            }
            for(int i = end + 1; i % BOARD_SIZE != 0 && letters[i] != '.' && letters[i] != '*'; i++){
                end = i;
            }
            char[] word = new char[end - start + 1];
            for(int i = 0; i < word.length; i++){
                word[i] = letters[spot.getI() + i];
            }
            return word;
        }
        if(checkColumn){
            int start = spot.column;
            int end = spot.column;
            for(int i = start - BOARD_SIZE; i / BOARD_SIZE >= 0 && letters[i] != '.' && letters[i] != '*'; i -= BOARD_SIZE){
                start = i;
            }
            for(int i = end + BOARD_SIZE; i /BOARD_SIZE < BOARD_SIZE && letters[i] != '.' && letters[i] != '*'; i += BOARD_SIZE){
                end = i;
            }
            char[] word = new char[end - start + 1];
            for(int i = 0; i < word.length; i += BOARD_SIZE){
                word[i] = letters[spot.getI() + i];
            }
            return word;
        }
        throw new IllegalArgumentException("Either checkRow or checkColumn must be true.");
    }

    private static boolean isWord(char[] rowOrColumn){
        String word = new String(rowOrColumn);
        word = word.replaceAll("^\\.*([^\\.](?:.*[^\\.])?)\\.*$", "$1");
        if(!word.matches("^[A-Z]+$")) throw new InvalidRequestException("Invalid Move. Illegal characters placed on board.");
        return AnagramService.isWord(word.toLowerCase());
    }

    public static char getRandomChar(){
        double[] weights = new double[] { 0.03d, 0.05d, 0.08d, 0.12d, 0.16d, 0.18d, 0.18d, 0.18d };
        String[] charSets = new String[] { "G", "JKQXZ", "O", "E", "DLSU", "AI", "NRT", "BCFHMPVWY" };

        int counter = 0;
        for (double r = Math.random(); counter < weights.length - 1; counter++){
            r -= weights[counter];
            if (r <= 0.0) break;
        }

        Random rand = new Random();
        return charSets[counter].charAt(rand.nextInt(100) % charSets[counter].length());
    }
}