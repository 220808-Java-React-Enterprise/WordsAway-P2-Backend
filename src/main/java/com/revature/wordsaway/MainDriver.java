package com.revature.wordsaway;

import com.revature.wordsaway.services.AIService;
import com.revature.wordsaway.services.AnagramService;

import java.util.*;

//@SpringBootApplication
public class MainDriver {
    static char[] tray = "uitsgre".toLowerCase().toCharArray();
    static char[] letters = new char[16*16];

    public static void main(String[] args) {
        //SpringApplication.run(MainDriver.class, args);

        Arrays.fill(letters, '.');
        letters[50] = 'l';
        letters[66] = 'u';
        letters[82] = 'm';
        letters[98] = 'b';
        letters[114] = 'e';
        letters[130] = 'r';

        letters[52] = 'l';
        letters[68] = 'i';
        letters[84] = 'f';
        letters[100] = 't';

        letters[71] = 'd';
        letters[87] = 'r';
        letters[103] = 'e';
        letters[119] = 'a';
        letters[135] = 'm';

        letters[57] = 's';
        letters[73] = 'e';
        letters[89] = 'a';
        letters[105] = 's';
        letters[121] = 'o';
        letters[137] = 'n';

        letters[180] = 'r';
        letters[181] = 'o';
        letters[182] = 'u';
        letters[183] = 't';
        letters[184] = 'e';

        new AIService(letters, tray).start();
    }
}