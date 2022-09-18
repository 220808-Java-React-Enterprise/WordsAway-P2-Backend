package com.revature.wordsaway.services;

import com.revature.wordsaway.models.Board;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.revature.wordsaway.utils.Constants.BOARD_SIZE;

@Service
public class AIService { // TODO improve bot/make harder bot
    static Map<String, Integer> wordCounter = new TreeMap<>();

    public static Map<String, Integer> getWordCounter(){ return wordCounter; }
    private final Board board;
    private final char[] letters;
    private char[] tray;

    private final static Map<Integer, boolean[]> existingList = new HashMap<>();

    private class WordAndLocation{
        private int location;
        private String word;
        private int fireBalls;
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

        int counter = 0;
        // Get a final list of all moves in give row or col
        while (counter < BOARD_SIZE - 2) {
            existingLetters = getExistingLetters(curr, increment);
            validWords = getWordList(existingLetters, curr, increment);
            finalList.addAll(getWordListAndLocation(validWords, curr, increment));

            while (isLoop(col, start, curr) && letters[curr] != '.' && letters[curr] != '*'){
                curr += increment;
                counter++;
            }
            curr += increment;
            counter++;
        }

        // Check if list is empty
        if (finalList.isEmpty()){
            Board newBoard = startEasyBot(startTime);

            // If board has made no changes, replace tray, and return board
            if (newBoard == null){
                for (int i = 0; i < tray.length; i++)
                    tray[i] = BoardService.getRandomChar();
                board.setTray(tray);
                return board;
            }
            return newBoard;
        }

        // Get random answer and play it
        WordAndLocation wl = finalList.get(rand.nextInt(finalList.size()));
        return finalizeMove(board, wl, increment);
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
        List<String> incomingWords;

        int rowOrCol = increment == BOARD_SIZE ? start / BOARD_SIZE : start % BOARD_SIZE,
            maxWordLength = tray.length - pattern.replaceAll("[A-Z]", "").length() + pattern.length(),
            wordLength = Math.min(maxWordLength, BOARD_SIZE - rowOrCol);

        // Loop for all possible words in that given space
        do {
            // todo Look into page not found error; maybe
            incomingWords = AnagramService.getAllList(String.valueOf(tray), pattern, wordLength);
            if (incomingWords != null)
                words.addAll(incomingWords);

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
        int index, fireBalls = 0;
        char[] c;
        boolean lessThan, greaterThan, col = increment == BOARD_SIZE;
        // Switch increment for validity
        int newIncrement = col ? 1 : BOARD_SIZE;

        // Loop for each word
        for (String word : words){
            // Index for each char in word
            index = 0;
            c = word.toCharArray();
            exit:{
                // Loop to validate word
                for (int j = start; index < word.length(); j += increment){
                    // Clear sb
                    sb.delete(0, sb.length());
                    // Check if letter fits in current location
                    if (letters[j] != '.' && letters[j] != c[index]) break exit;

                    // Letters in the neg direction
                    if (!(j - newIncrement < 0))
                        for (int cw = j - newIncrement; isLoop(!col, j, cw) && letters[cw] != '.' && letters[cw] != '*'; cw -= newIncrement)
                            sb.insert(0, sb.length() != 0 ? letters[cw] : String.valueOf(letters[cw]) + c[index]);

                    // Letters in the pos direction
                    if (!(j + newIncrement > letters.length - 1))
                        for (int cw = j + newIncrement; isLoop(!col, j, cw) && letters[cw] != '.' && letters[cw] != '*'; cw += newIncrement)
                            sb.append(sb.length() != 0 ? letters[cw] : String.valueOf(c[index]) + letters[cw]);

                    // Validate word
                    if (sb.length() > 0 && !AnagramService.isWord(sb.toString().toLowerCase())) break exit;

                    //if (sb.length() > 2) fireBalls++;

                    index++;
                }
                wl = new WordAndLocation();
                wl.fireBalls = fireBalls;
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

    private Board finalizeMove(Board board, WordAndLocation wl, int increment){
        StringBuilder sb = new StringBuilder(String.valueOf(tray));
        int counter = 0;
        // Word being played
        char[] c = wl.word.toCharArray();
        for (int i = wl.location; counter < c.length; i += increment) {
            if (letters[i] == '.' || letters[i] == '*'){
                letters[i] = c[counter];
                sb.setCharAt(sb.indexOf(String.valueOf(c[counter])), BoardService.getRandomChar());
                tray = sb.toString().toCharArray();
            } else board.setFireballs(board.getFireballs() + 1); // todo fix increment of fireball
            counter++;
        }
        board.setLetters(letters);
        board.setTray(tray);
        wordCounter.put(wordCounter.size() + 1 + "-" + wl.word, wl.location);
        return board;
    }
}