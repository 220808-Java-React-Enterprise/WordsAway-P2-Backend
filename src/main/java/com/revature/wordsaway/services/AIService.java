package com.revature.wordsaway.services;

import com.revature.wordsaway.models.Board;
import org.springframework.stereotype.Service;

import java.util.*;
import static com.revature.wordsaway.utils.Constants.BOARD_SIZE;

@Service
public class AIService {

    private final Board board;
    private final char[] letters;
    private char[] tray; // TODO change to and instance of a board

    private final static Map<Integer, boolean[]> existingList = new HashMap<>();

    class WordAndLocation{
        public int location;
        public String word;
        @Override
        public String toString() {
            return "ValidWord{" +
                    "location=" + location +
                    ", word='" + word + '\'' +
                    '}';
        }
    }

    public AIService(Board board){
        this.board = board;
        this.letters = board.getLetters();
        this.tray = board.getTray();
    }

    public Board startEasyBot(long startTime){
        // If bot has taken longer than 25 seconds leave
        if (System.currentTimeMillis() - startTime > 25000) return null;
        // Get random number from 0-31
        Random rand = new Random();
        // Set seed to system time
        rand.setSeed(rand.nextInt());

        // Get a random colum or row
        int start;
        boolean col = (start = rand.nextInt(32)) % 2 == 0;
        start /= 2;

        // Establish increment variable
        int increment;
        if (col) increment = BOARD_SIZE;
        else { increment = 1; start *= BOARD_SIZE; }

        int curr = start;
        List<WordAndLocation> finalList = new ArrayList<>();
        List<String> validWords;
        String existingLetters;

        // Get a final list of all moves in give row or col
        while (isLoop(col, start, curr)) {
            existingLetters = getExistingLetters(curr, increment);
            validWords = getWordList(existingLetters, curr, increment);
            finalList.addAll(getWordListAndLocation(validWords, curr, increment));

            while (isLoop(col, start, curr) && letters[curr] != '.' && letters[curr] != '*')
                curr += increment;
            curr += increment;
        }
        // Check if list is empty
        if (finalList.isEmpty()){
            Board newBoard = startEasyBot(startTime);

            // If board has made no changes, replace tray, and return board
            if (newBoard == null)
                for (int i = 0; i < tray.length; i++)
                    tray[i] = (char) (rand.nextInt(26) + 65);
            board.setTray(tray);
            return board;
        }
        WordAndLocation wl = finalList.get(rand.nextInt(finalList.size()));

        return finalizeMove(board, wl, rand, increment);
    }

    private String getExistingLetters(int start, int increment){
        StringBuilder sb = new StringBuilder();
        StringBuilder spacer = new StringBuilder();

        // Get the beginning of the loop
        int spacerCounter = 0, counter = increment == BOARD_SIZE ? start / BOARD_SIZE : start % BOARD_SIZE;

        // Loop until end of colum or row
        for (int i = start; spacerCounter < tray.length && counter < BOARD_SIZE; i += increment){
            // Check if we are at a '.'
            if (letters[i] != '.' && letters[i] != '*') {
                sb.append(spacer.append(letters[i]));
                spacer.delete(0, spacer.length());
            }
            // If there is a value in sb then add a spacer to spacer
            else {
                spacer.append("_");
                spacerCounter++;
            }
            counter++;
        }
        return sb.toString();
    }

    private List<String> getWordList(String pattern, int start, int increment) {
        List<String> words = new ArrayList<>();

        int rowOrCol = increment == BOARD_SIZE ? start / BOARD_SIZE : start % BOARD_SIZE,
            maxWordLength = tray.length + pattern.replace("_", "").length(),
            wordLength = Math.min(BOARD_SIZE - rowOrCol, maxWordLength);

        // Loop for all possible words in that given space
        do {
            words.addAll(AnagramService.getAllList(String.valueOf(tray), pattern, wordLength));
            if (pattern.lastIndexOf("_") == -1) break;
            pattern = pattern.substring(0, pattern.lastIndexOf("_"));
            wordLength = pattern.length();
        } while (pattern.length() > 2);

        return words;
    }

    private List<WordAndLocation> getWordListAndLocation(List<String> words, int start, int increment){
        List<WordAndLocation> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        WordAndLocation wl;
        int index;
        char[] c;
        boolean lessThan, greaterThan, col = increment == BOARD_SIZE;
        // Switch increment for validity
        int newIncrement = col ? 1 : BOARD_SIZE;

        // Loop for each word
        for (String word : words){
            // Index for each char in word
            index = 0;
            c = word.toLowerCase().toCharArray();
            exit:{
                // Loop to validate word
                for (int j = start; index < word.length(); j += increment){
                    // Clear sb
                    sb.delete(0, sb.length());
                    // Check if letter fits in current location
                    if (letters[j] != '.' && letters[j] != c[index]) break exit;

                    // Declare booleans
                    lessThan = j - newIncrement < 0;
                    greaterThan = j + newIncrement > letters.length - 1;

                    // Letters in the neg direction
                    if (!lessThan)
                        for (int cw = j - newIncrement; isLoop(!col, j, cw) && letters[cw] != '.' && letters[cw] != '*'; cw -= newIncrement)
                            sb.insert(0, sb.length() != 0 ? letters[cw] : String.valueOf(letters[cw]) + c[index]);

                    // Letters in the pos direction
                    if (!greaterThan)
                        for (int cw = j + newIncrement; isLoop(!col, j, cw) && letters[cw] != '.' && letters[cw] != '*'; cw += newIncrement)
                            sb.append(sb.length() != 0 ? letters[cw] : String.valueOf(c[index]) + letters[cw]);

                    // Validate word
                    if (sb.length() > 0 && !AnagramService.isWord(sb.toString().toLowerCase())) break exit;

                    index++;
                }
                wl = new WordAndLocation();
                wl.location = start;
                wl.word = word;
                list.add(wl);
            }
        }
        return list;
    }

    private boolean isLoop(boolean col, int start, int curr){
        if (col)
            return curr < BOARD_SIZE * BOARD_SIZE && curr >= 0;
        return start / BOARD_SIZE == curr / BOARD_SIZE;
    }

    private Board finalizeMove(Board board, WordAndLocation wl, Random rand, int increment){
        int counter = 0;
        char[] c = wl.word.toCharArray();
        for (int i = wl.location; counter < c.length; i += increment) {
            if (letters[i] == '.' || letters[i] == '*'){
                letters[i] = c[counter];
                tray = String.valueOf(tray).replace(c[counter], (char) (rand.nextInt(26) + 65)).toCharArray();
            }
            else board.setFireballs(board.getFireballs() + 1);
            counter++;
        }
        board.setLetters(letters);
        board.setTray(tray);

        return board;
    }
}