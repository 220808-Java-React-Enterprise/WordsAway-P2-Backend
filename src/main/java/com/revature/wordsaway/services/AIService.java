package com.revature.wordsaway.services;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AIService {

    private final char[] letters, tray;

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
                    allPossibleWords = null;//AnagramService.getAll(String.valueOf(tray));
                else
                    allPossibleWords = null;//AnagramService.getAll(map.get(i));

                //bestWords.add(getBestWord(allPossibleWords, col, i));
            }
            col = false;
        }
        return bestWords;
    }

    private String getBestWord(List<String> words, boolean col, int rowOrColNumber){


        return null;
    }
}