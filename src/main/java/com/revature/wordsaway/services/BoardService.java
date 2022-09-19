package com.revature.wordsaway.services;

import com.revature.wordsaway.dtos.requests.BoardRequest;
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

    public static List<Board> getByGameID(UUID gameID) throws InvalidRequestException {
        List<Board> boards = boardRepository.findBoardByGameID(gameID);
        if(boards == null || boards.size() == 0) throw new InvalidRequestException("No boards with gameID " + gameID + " found.");
        return boards;
    }

    public static Board getOpposingBoard(Board board) {
        Board opposingBoard =  boardRepository.findOpposingBoardByIDAndGameID(board.getId(), board.getGameID());
        if(opposingBoard == null) throw new InvalidRequestException("No boards opposing " + board.getGameID() + " found.");
        return opposingBoard;
    }

    public static char getRandomChar() {
        double[] weights = new double[]{0.03d, 0.05d, 0.08d, 0.12d, 0.16d, 0.18d, 0.18d, 0.18d};
        String[] charSets = new String[]{"G", "JKQXZ", "O", "E", "DLSU", "AI", "NRT", "BCFHMPVWY"};
        int counter = 0;
        for (double r = Math.random(); counter < weights.length - 1; counter++) {
            r -= weights[counter];
            if (r <= 0.0) break;
        }
        Random rand = new Random();
        return charSets[counter].charAt(rand.nextInt(100) % charSets[counter].length());
    }

    public static void validateMove(BoardRequest request) throws InvalidRequestException {
        char[] oldLetters = getByID(request.getBoardID()).getLetters();
        char[] newLetters = request.getLayout();
        List<ChangeSpot> changeSpots = new ArrayList<>();
        boolean checkRow = false, checkColumn = false, asterisk = false;
        loop: for(int i = 0; i < oldLetters.length; i++){
            if(oldLetters[i] != newLetters[i]){
                ChangeSpot spot = new ChangeSpot(i);
                for(ChangeSpot existingSpot : changeSpots){
                    if(existingSpot.equals(spot)) continue loop;
                }
                if(asterisk) throw new InvalidRequestException("Invalid Move. Fireball may only be placed alone.");
                else if(newLetters[i] == '*' && changeSpots.size() == 0) asterisk = true;
                else if(newLetters[i] == '*') throw new InvalidRequestException("Invalid Move. Fireball may only be placed alone.");
                changeSpots.add(spot);
                if (changeSpots.size() > 2) {
                    if(checkRow && changeSpots.get(changeSpots.size() - 1).row != changeSpots.get(0).row)
                        throw new InvalidRequestException("Invalid Move. Only one word may be placed at a time.");
                    if(checkColumn && changeSpots.get(changeSpots.size() - 1).column != changeSpots.get(0).column)
                        throw new InvalidRequestException("Invalid Move. Only one word may be placed at a time.");
                } else if(changeSpots.size() == 2){
                    checkRow = changeSpots.get(0).row == changeSpots.get(1).row;
                    checkColumn = changeSpots.get(0).column == changeSpots.get(1).column;
                    if(!checkRow && !checkColumn)
                        throw new InvalidRequestException("Invalid Move. All tiles must be placed in either the same row or same column.");
                }
            }
        }
        if(changeSpots.size() == 0) throw new InvalidRequestException("Invalid Move. Must be some change in boards.");
        if(changeSpots.size() == 1){
            if (!asterisk && !isWord(findConnectedWord(newLetters, changeSpots.get(0), true, false))
                    && !isWord(findConnectedWord(newLetters, changeSpots.get(0), false, true)))
                throw new InvalidRequestException("Invalid Move. Placed tiles do not form valid word.");
            return;
        }
        if(checkRow){
            for(int i = changeSpots.get(0).getI() + 1; i <= changeSpots.get(changeSpots.size() - 1).getI(); i++){
                if(newLetters[i] == '.' || newLetters[i] == '*') throw new InvalidRequestException("Invalid Move. Only one word may be placed at a time.");
            }
        } else if (checkColumn) {
            for(int i = changeSpots.get(0).getI() + BOARD_SIZE; i <= changeSpots.get(changeSpots.size() - 1).getI(); i += BOARD_SIZE){
                if(newLetters[i] == '.' || newLetters[i] == '*') throw new InvalidRequestException("Invalid Move. Only one word may be placed at a time.");
            }
        }
        if(!isWord(findConnectedWord(newLetters, changeSpots.get(0), checkRow, checkColumn)))
            throw new InvalidRequestException("Invalid Move. Placed tiles do not form valid word.");
        for(ChangeSpot spot : changeSpots){
            char[] word = findConnectedWord(newLetters, spot, !checkRow, !checkColumn);
            if(word.length > 1 && !isWord(word))
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

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof ChangeSpot)) return false;
            ChangeSpot spot = (ChangeSpot) obj;
            return spot.row == row && spot.column == column;
        }
        /**@Override
        public String toString(){
            return "(" + row + "," + column + ")";
         }**/
    }

    private static char[] findConnectedWord(char[] letters, ChangeSpot spot, boolean checkRow, boolean checkColumn){
        if(checkRow && checkColumn) throw new IllegalArgumentException("Both checkRow and checkColumn may not be true.");
        if(checkRow){
            int start = spot.getI();
            int end = spot.getI();
            int rowStart = spot.row * BOARD_SIZE;
            for(int i = start - 1; i >= rowStart && letters[i] != '.' && letters[i] != '*'; i--){
                start = i;
            }
            for(int i = end + 1; i < rowStart + BOARD_SIZE && letters[i] != '.' && letters[i] != '*'; i++){
                end = i;
            }
            char[] word = new char[end - start + 1];
            for(int i = 0; i < word.length; i++){
                word[i] = letters[start + i];
            }
            return word;
        }
        if(checkColumn){
            int start = spot.getI();
            int end = spot.getI();
            for(int i = start - BOARD_SIZE; i >= 0 && letters[i] != '.' && letters[i] != '*'; i -= BOARD_SIZE){
                start = i;
            }
            for(int i = end + BOARD_SIZE; i < BOARD_SIZE * BOARD_SIZE && letters[i] != '.' && letters[i] != '*'; i += BOARD_SIZE){
                end = i;
            }
            char[] word = new char[(end - start) / BOARD_SIZE + 1];
            for(int i = 0; i < word.length; i++){
                word[i] = letters[start + (i * BOARD_SIZE)];
            }
            return word;
        }
        throw new IllegalArgumentException("Either checkRow or checkColumn must be true.");
    }

    private static boolean isWord(char[] rowOrColumn){
        String word = new String(rowOrColumn);
        if(!word.matches("^[A-Z]+$")) throw new InvalidRequestException("Invalid Move. Illegal characters placed on board.");
        return AnagramService.isWord(word.toLowerCase());
    }


    public static boolean[] getHits(char[] letters){
        boolean[] hits = new boolean[BOARD_SIZE * BOARD_SIZE];
        for (int i = 0; i < hits.length; i++) {
            switch (letters[i]) {
                case 'A': case 'B': case 'C':
                case 'D': case 'E': case 'G':
                case 'H': case 'I': case 'L':
                case 'M': case 'N': case 'O':
                case 'P': case 'S': case 'T':
                case 'R': case 'U': case '*':
                    hits[i] = true;
                    break;
                case 'F': case 'K': case 'V':
                case 'Y': case 'W':
                    makeAdjacentTrue(hits, i);
                    break;
                case 'J': case 'Q': case 'X':
                case 'Z':
                    int startingRow = i / BOARD_SIZE;
                    for (int j = i; j >= 0 && j / BOARD_SIZE == startingRow; j--) {
                        if(letters[j] != '.' && letters[j] != '*'){
                            makeAdjacentTrue(hits, j);
                        }
                    }
                    for (int j = i + 1; j < BOARD_SIZE*BOARD_SIZE && j / BOARD_SIZE == startingRow; j++) {
                        if(letters[j] != '.' && letters[j] != '*'){
                            makeAdjacentTrue(hits, j);
                        }
                    }
                    int startingColumn = i / BOARD_SIZE;
                    for (int j = i; j >= 0 && j % BOARD_SIZE == startingColumn; j-= BOARD_SIZE) {
                        if(letters[j] != '.' && letters[j] != '*'){
                            makeAdjacentTrue(hits, j);
                        }
                    }
                    for (int j = i + BOARD_SIZE; j < BOARD_SIZE*BOARD_SIZE && j % BOARD_SIZE == startingColumn; j += BOARD_SIZE) {
                        if(letters[j] != '.' && letters[j] != '*'){
                            makeAdjacentTrue(hits, j);
                        }
                    }
                    break;
            }
        }
        return hits;
    }

    private static void makeAdjacentTrue(boolean[] hits, int i){
        hits[i] = true;
        if (i - 1 >= 0) hits[i - 1] = true;
        if (i + 1 < BOARD_SIZE * BOARD_SIZE) hits[i + 1] = true;
        if (i - BOARD_SIZE >= 0) hits[i - BOARD_SIZE] = true;
        if (i + BOARD_SIZE < BOARD_SIZE * BOARD_SIZE) hits[i + BOARD_SIZE] = true;
    }
}