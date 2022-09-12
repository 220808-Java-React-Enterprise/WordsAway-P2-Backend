package com.revature.wordsaway.services;

import com.revature.wordsaway.dtos.responses.AnagramResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AnagramService {

    public static String getBest(String letters){
        return new RestTemplate().getForObject("http://www.anagramica.com/best/" + letters, String.class);
    }

    public static String getAll(String letters){
        return new RestTemplate().getForObject("http://www.anagramica.com/all/" + letters, String.class);
    }

    public static boolean isWord(String letters) {
        try {
            return new RestTemplate().getForObject("http://www.anagramica.com/lookup/" + letters, AnagramResponse.class).getFound() == 1;
        } catch (NullPointerException e) {
            return false;
        }
    }
}
