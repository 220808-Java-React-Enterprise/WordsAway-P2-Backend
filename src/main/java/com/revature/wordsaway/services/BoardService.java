package com.revature.wordsaway.services;

import com.revature.wordsaway.models.Board;
import com.revature.wordsaway.models.User;
import com.revature.wordsaway.repositories.BoardRepository;
import com.revature.wordsaway.utils.customExceptions.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Arrays.fill(blankArr, '.');
        Board board = new Board(
                UUID.randomUUID(),
                user,
                "abcdefg".toCharArray(), //TODO make random starting tray
                0,
                blankArr,
                blankArr,
                gameID,
                isActive
        );
        boardRepository.save(board);
        return board;
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

    //TODO probably delete this at the end
    public static void deleteAll(){
        boardRepository.deleteAll();
    }

    public static void validateMove(UUID boardID, char[] move) throws InvalidRequestException {
        //TODO try to simplify this method.
        Board oldBoard = getByID(boardID);
        Board newBoard = new Board(oldBoard, move);
        Set<Integer> oneDiffs = new HashSet<>();
        List<char[]> oneDiffsChanges = new ArrayList<>();
        char[] moreDiffRow = null;
        char[] moreDiffColumn = null;
        int differences, startChange, endChange;
        for(int i = 0; i < BOARD_SIZE; i++){
            differences = 0; startChange = -1; endChange = -1;
            for(int j = 0; j < BOARD_SIZE; j++){
                if(oldBoard.getRow(i)[j] != newBoard.getRow(i)[j]) {
                    if(endChange != -1) throw new InvalidRequestException("Invalid Move. Can not place more than one word at a time.");
                    if(oldBoard.getRow(i)[j] != '.') throw new InvalidRequestException("Invalid Move. Can not place tile on non-empty spot.");
                    if(startChange == -1) startChange = j;
                    differences++;
                }else if(startChange != -1 && newBoard.getRow(i)[j] == '.') endChange = j;
            }
            if(differences == 1) oneDiffs.add(i * BOARD_SIZE + startChange);
            for(int j = startChange; j >= 0; j--){
                if(newBoard.getRow(i)[j] != '.') startChange = j;
                else break;
            }
            if(startChange != -1){
                if(endChange == -1) endChange = BOARD_SIZE;
                for(int j = endChange; j < BOARD_SIZE; j++){
                    if(newBoard.getRow(i)[j] != '.') endChange = j;
                    else break;
                }
            }
            if(differences == 1) {
                char[] diffChanges = new char[endChange - startChange];
                for(int j = 0; j < diffChanges.length; j++){
                    diffChanges[j] = newBoard.getRow(i)[startChange + j];
                }
                oneDiffsChanges.add(diffChanges);
            }else if (differences > 1) {
                if(moreDiffRow != null) throw new InvalidRequestException("Invalid Move. More than one row may not be placed at once.");
                moreDiffRow = new char[endChange - startChange];
                for(int j = 0; j < moreDiffRow.length; j++){
                    moreDiffRow[j] = newBoard.getRow(i)[startChange + j];
                }
            }
            differences = 0; startChange = -1; endChange = -1;
            for(int j = 0; j < BOARD_SIZE; j++){
                if(oldBoard.getColumn(i)[j] != newBoard.getColumn(i)[j]) {
                    if(endChange != -1) throw new InvalidRequestException("Invalid Move. Can not place more than one word at a time.");
                    if(oldBoard.getColumn(i)[j] != '.') throw new InvalidRequestException("Invalid Move. Can not place tile on non-empty spot.");
                    if(startChange == -1) startChange = j;
                    differences++;
                }else if(startChange != -1 && newBoard.getColumn(i)[j] == '.') endChange = j;
            }
            if(differences == 1) oneDiffs.add(i + startChange * BOARD_SIZE);
            for(int j = startChange; j >= 0; j--){
                if(newBoard.getColumn(i)[j] != '.')startChange = j;
                else break;
            }
            if(startChange != -1) {
                if (endChange == -1) endChange = BOARD_SIZE;
                for (int j = endChange; j < BOARD_SIZE; j++) {
                    if (newBoard.getColumn(i)[j] != '.') endChange = j;
                    else break;
                }
            }
            if(differences == 1) {
                char[] diffChanges = new char[endChange - startChange];
                for(int j = 0; j < diffChanges.length; j++){
                    diffChanges[j] = newBoard.getColumn(i)[startChange + j];
                }
                oneDiffsChanges.add(diffChanges);
            }else if (differences > 1) {
                if(moreDiffColumn != null) throw new InvalidRequestException("Invalid Move. More than one column may not be placed at once.");
                moreDiffColumn = new char[endChange - startChange];
                for(int j = 0; j < moreDiffColumn.length; j++){
                    moreDiffColumn[j] = newBoard.getColumn(i)[startChange + j];
                }
            }
        }
        if(moreDiffRow != null && moreDiffColumn != null) throw new InvalidRequestException("Invalid Move. More than one column or row may not be placed at once.");
        if(moreDiffRow != null) {
            if (!isWord(moreDiffRow)) throw new InvalidRequestException("Invalid Move. Placed tiles do not form valid word.");
            else return;
        }else if(moreDiffColumn != null) {
            if(!isWord(moreDiffColumn)) throw new InvalidRequestException("Invalid Move. Placed tiles do not form valid word.");
            else return;
        }else if(oneDiffs.size() > 1) throw new InvalidRequestException("Invalid Move. More than one tile in different rows and columns may not be placed at once.");
        else if (oneDiffs.size() > 0){
            int index = (int) oneDiffs.toArray()[0];
            char c = newBoard.getRow(index / BOARD_SIZE)[index % BOARD_SIZE];
            if(c == '*') return;
            for(char[] change : oneDiffsChanges){
                if(!isWord(change)) throw new InvalidRequestException("Invalid Move. Placed tiles do not form valid word.");
            }
        }
        throw new InvalidRequestException("Invalid Move. Must be some change in boards.");
    }

    private static boolean isWord(char[] rowOrColumn){
        String word = new String(rowOrColumn);
        word = word.replaceAll("^\\.*([^\\.](?:.*[^\\.])?)\\.*$", "$1");
        System.out.println(word); //TODO remove this at the end
        if(!word.matches("^[A-Z]+$")) throw new InvalidRequestException("Invalid Move. Illegal characters placed on board.");
        return AnagramService.isWord(word.toLowerCase());
    }
}
