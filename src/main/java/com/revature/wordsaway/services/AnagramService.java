package com.revature.wordsaway.services;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.revature.wordsaway.dtos.responses.AnagramResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnagramService {
    private static RestTemplate restTemplate = new RestTemplate();

    public AnagramService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public static String getBest(String letters){
        try {
            return restTemplate.getForObject("http://www.anagramica.com/best/" + letters, String.class);
        }catch (HttpClientErrorException e){
            return "";
        }
    }

    public static String getAll(String letters){
        try {
            return restTemplate.getForObject("http://www.anagramica.com/all/" + letters, String.class);
        }catch (HttpClientErrorException e){
            return "";
        }
    }

    public static boolean isWord(String letters) {
        try {
            return restTemplate.getForObject("http://www.anagramica.com/lookup/" + letters, AnagramResponse.class).getFound() == 1;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @ExceptionHandler(value = IOException.class)
    public static List<String> getAllList(String letters, String pattern, int wordLength){
        List<String> words = new ArrayList<>();
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        String pageURL = !pattern.equals("")
                ? "https://anagram-solver.io/words-for/" + letters + "/pattern/" + pattern + "/?dictionary=otcwl"
                : "https://anagram-solver.io/words-for/" + letters + "/pattern/___/?dictionary=otcwl";

        try {
            // Make request
            HtmlPage page = client.getPage(pageURL);

            // Get all anagrams
            List<HtmlElement> items = page.getByXPath("//div[@class='wordblock']/a");

            for (HtmlElement item : items){
                // Save to a list
                String word = item.asNormalizedText().toUpperCase();

                if (word.length() == 2) break;

                // Check if it's the pattern
                if (!word.equals(pattern.replace("_", "")))
                    // Skip if length of word is greater than what we want
                    if (word.length() <= wordLength)
                        words.add(word);
            }
        } catch (IOException | FailingHttpStatusCodeException e){ // todo handle exceptions
            return null;
        }
        return words;
    }
}
