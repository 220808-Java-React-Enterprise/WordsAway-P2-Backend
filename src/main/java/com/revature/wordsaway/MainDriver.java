package com.revature.wordsaway;

import com.revature.wordsaway.services.BoardService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.revature.wordsaway.models.Board;
import com.revature.wordsaway.models.User;
import com.revature.wordsaway.services.AIService;

import java.util.*;

@SpringBootApplication
public class MainDriver {
    public static void main(String[] args) {
        //SpringApplication.run(MainDriver.class, args);

        char[] tray = new char[7];
        char[] letters = new char[16*16];

        Arrays.fill(letters, '.');


        for (int i = 0; i < tray.length; i++)
            tray[i] = BoardService.getRandomChar();

        User user = new User("RJamesRJ", "password1", "salt", "robertjames@gmail.com", 1000, 0, 0, false);
        Board board = new Board(UUID.randomUUID(), user, tray, 0, new char[2], letters, UUID.randomUUID(), true);


        long total = 0;

        for (int i = 0; i < 50; i++){
            long start = System.currentTimeMillis();

            board = new AIService(board).startEasyBot(start);

            total += (System.currentTimeMillis() - start) / 1000;
        }

        letters = board.getLetters();
        int count = 0;
        for (int j = 0; j < 16; j++) {
            for (int k = 0; k < 16; k++) {
                System.out.print(letters[count] + ", ");
                count++;
            }
            System.out.println();
        }
        System.out.println(total / 50);
        System.out.println(board.getFireballs());
        System.out.println(AIService.getWordCounter());
        System.out.println(AIService.getWordCounter().size());
    }
}