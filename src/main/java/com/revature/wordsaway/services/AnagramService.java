package com.revature.wordsaway.services;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.revature.wordsaway.dtos.responses.AnagramResponse;
import com.revature.wordsaway.utils.customExceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnagramService {

    public static String getBest(String letters){
        return new RestTemplate().getForObject("http://www.anagramica.com/best/" + letters, String.class);
    }

//    public static String getAll(String letters){
//        return new RestTemplate().getForObject("http://www.anagramica.com/all/" + letters, String.class);
//    }

    public static boolean isWord(String letters) {
        try {
            return new RestTemplate().getForObject("http://www.anagramica.com/lookup/" + letters, AnagramResponse.class).getFound() == 1;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @ExceptionHandler(value = IOException.class)
    public static List<String> getAll(String letters){
        List<String> words = new ArrayList<>();
        // Instantiate a client
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        try {
            // Make request
            HtmlPage page = client.getPage("https://anagram-solver.io/words-for/" + letters + "/?dictionary=otcwl");

            // Get all anagrams
            List<HtmlElement> items = page.getByXPath("//div[@class='wordblock']/a");

            for (HtmlElement item : items){
                // Save to a list
                String word = item.asNormalizedText();

                // Exit if length of word is 2
                if (word.length() == 2) break;

                words.add(word);
            }
        } catch (IOException e){
            throw new NotFoundException("Page not found");
        }
        return words;
    }
}
