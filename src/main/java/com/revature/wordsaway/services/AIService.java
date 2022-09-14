package com.revature.wordsaway.services;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AIService {

    private final char[] letters, tray; // TODO change to full board

    public AIService(char[] letters, char[] tray){
        this.letters = letters;
        this.tray = tray;
    }
    public void start(){
        // Get all possible additional letters
        Map<String, Map<Integer, String>> possibleTrays = getExistingLetters();

        // Best word for every colum and row
        List<String> bestWordList = getBestWordList(possibleTrays);
    }

    private Map<String, Map<Integer, String>> getExistingLetters(){
        Map<String, Map<Integer, String>> map = new HashMap<>();
        Map<Integer, String> row = new HashMap<>();
        Map<Integer, String> col = new HashMap<>();

        for (int i = 0; i < letters.length; i++){
            if (letters[i] != '.') {
                row.put(i / 16, row.containsKey(i / 16) ? row.get(i / 16) + letters[i] : String.valueOf(tray) + letters[i]);
                col.put(i % 16, col.containsKey(i % 16) ? col.get(i % 16) + letters[i] : String.valueOf(tray) + letters[i]);
            }
        }

        map.put("row", row);
        map.put("col", col);
        return map;
    }

    private List<String> getBestWordList(Map<String, Map<Integer, String>> possibleTrays){
        List<String> bestWords = new ArrayList<>();
        List<String> allPossibleWords;
        boolean col = true;

        for (Map<Integer, String> map : possibleTrays.values()){
            for (int i = 0; i < 16; i++){
                if (!map.containsKey(i))
                    allPossibleWords = null;// TODO AnagramService.getAllList(String.valueOf(tray));
                else
                    allPossibleWords = null;// TODO AnagramService.getAllList(map.get(i));

                bestWords.add(getBestWord(allPossibleWords, col, i));
            }
            col = false;
        }
        return bestWords;
    }

    private String getBestWord(List<String> words, boolean col, int rowOrColNumber){
        Map<Integer, String> validWords = new HashMap<>();
        String best = "";

        exit:{
            if (col){
                for(int i = rowOrColNumber; i < letters.length; i += 16){
                    for (String word : words){
                        // If the word no longer fits exit
                        if (word.length() - i / 16 < 0) break exit;

                        int index = 0;
                        char[] c = word.toCharArray();
                        for (int j = i; j < word.length(); j += 16){
                            // Check if letter fits in current location
                            if (letters[j] != '.' && letters[j] != c[index]) break;
                        }
                    }
                }
            }
            else {
                int start = rowOrColNumber * 16;
                for (int i = start; i < start + 16; i++){
                    for (String word : words) {
                        // If the word no longer fits exit
                        if (word.length() - i % 16 < 0) break exit;
                    }
                }
            }
        }

        return null;
    }
}