package com.revature.wordsaway.services;

import com.revature.wordsaway.models.Board;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.revature.wordsaway.utils.Constants.BOARD_SIZE;

@Service
public class AIService {
    private final Board board;
    private final char[] letters;
    private char[] tray;

    private final List<WordAndLocation> finalList = new ArrayList<>();
    private List<String> validWords;
    private String existingLetters;
    // Get random number from 0-31
    Random rand = new Random(System.currentTimeMillis());

    private static class WordAndLocation{
        private int location;
        private String word;
    }

    public AIService(Board board){
        this.board = board;
        this.letters = board.getLetters();
        this.tray = board.getTray();
    }

    public char[] start(long startTime, int level){
        // If bot has taken longer than 25 seconds leave
        if (System.currentTimeMillis() - startTime > 25000) return null;
        int increment;

        switch (level){
            case 1: increment = easyBot(); break;
            case 3: increment = startHardBot(); break;
            default: increment = startMedBot(); break;
        }

        // Check if list is empty
        if (finalList.isEmpty()){
            char[] move = start(startTime, 1); // todo update once more bots are implemented
            // If board has made no changes, replace tray, and return board
            if (move == null){
                for (int i = 0; i < tray.length; i++)
                    tray[i] = BoardService.getRandomChar();
                board.setTray(tray);
                return null;
            }
            return move;
        }
        // Get random answer and play it
        WordAndLocation wl = finalList.get(rand.nextInt(finalList.size()));
        finalizeMove(wl, increment);
        return wl.word.toCharArray();
    }

    public Board setWorms(){
        char[] wormLetter = new char[] { 'A', 'B', 'C', 'S', 'D' };
        int[] wormArr = new int[] { 5, 4, 3, 3, 2 };
        char[] worms = board.getWorms();
        boolean col, flag;
        int start, curr, end, increment;

        for (int i = 0; i < wormArr.length;) {
            // Get a direction for the ship
            col = rand.nextInt(BOARD_SIZE + BOARD_SIZE) % 2 == 0;
            // Set the increment
            increment = col ? BOARD_SIZE : 1;
            // Get start and end of worm
            curr = start = rand.nextInt(BOARD_SIZE * BOARD_SIZE);
            end = start + wormArr[i] * increment;

            // Check if you can get to end
            if (isLoop(col, start, end)){
                flag = true;
                while (flag ? curr < end : curr >= start) {
                    if (worms[curr] != '.') {
                        if (!flag) worms[curr] = '.';
                        flag = false;
                    } else worms[curr] = wormLetter[i];

                    curr += flag ? increment : increment * -1;
                }
                if (flag) i++;
            }
        }
        return board;
    }

    private int easyBot(){
        // Get a random colum or row
        int start;
        boolean col = (start = rand.nextInt(BOARD_SIZE + BOARD_SIZE)) % 2 == 0;
        start /= 2;

        // Establish increment variable
        int increment;
        if (col) increment = BOARD_SIZE;
        else { increment = 1; start *= BOARD_SIZE; }

        int curr = start;
        int counter = 0;
        // Get a final list of all moves in give row or col
        while (counter < BOARD_SIZE - 2) {
            existingLetters = getExistingLetters(curr, increment);
            validWords = getWordList(existingLetters, curr, increment);
            finalList.addAll(getFinalWordListAndLocation(validWords, curr, increment));

            while (isLoop(col, start, curr) && letters[curr] != '.' && letters[curr] != '*'){
                curr += increment;
                counter++;
            }
            curr += increment;
            counter++;
        }
        // todo does fireball cost a turn?
        if (board.getFireballs() > 0 && rand.nextInt(100) % 20 == 0)
            shootFireBall();

        return increment;
    }

    private int startMedBot(){
        return 0;
        // todo implement medium bot
    }

    private int startHardBot(){
        return 0;
        // todo implement hard bot
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

    private List<WordAndLocation> getFinalWordListAndLocation(List<String> words, int start, int increment){
        List<WordAndLocation> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        WordAndLocation wl;
        int index;
        char[] c;
        boolean col = increment == BOARD_SIZE;
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
                    for (int cw = j - newIncrement; cw >= 0 && isLoop(!col, j, cw) && letters[cw] != '.' && letters[cw] != '*'; cw -= newIncrement)
                        sb.insert(0, sb.length() != 0 ? letters[cw] : String.valueOf(letters[cw]) + c[index]);

                    // Letters in the pos direction
                    for (int cw = j + newIncrement; cw < letters.length && isLoop(!col, j, cw) && letters[cw] != '.' && letters[cw] != '*'; cw += newIncrement)
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

    private void finalizeMove(WordAndLocation wl, int increment){
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
    }

    private void shootFireBall(){
        int target = rand.nextInt(BOARD_SIZE * BOARD_SIZE - 1);
        while (letters[target] >= 'A' && letters[target] <= 'Z')
            target = rand.nextInt(BOARD_SIZE * BOARD_SIZE - 1);
        letters[target] = '*';
        board.setFireballs(board.getFireballs() - 1);
    }

    private boolean isLoop(boolean col, int start, int curr){
        if (col)
            return curr < BOARD_SIZE * BOARD_SIZE && curr >= 0;
        return start / BOARD_SIZE == curr / BOARD_SIZE;
    }
}