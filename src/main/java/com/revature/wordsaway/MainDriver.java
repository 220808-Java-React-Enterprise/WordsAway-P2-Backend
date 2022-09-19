package com.revature.wordsaway;

import com.revature.wordsaway.services.BoardService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.revature.wordsaway.models.Board;
import com.revature.wordsaway.models.User;
import com.revature.wordsaway.services.AIService;

import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class MainDriver {
    public static void main(String[] args) throws InterruptedException {
        //SpringApplication.run(MainDriver.class, args);

        char[] tray = new char[7];
        char[] letters = new char[16*16];
        char[] worms = new char[16*16];

        Arrays.fill(letters, '.');

        for (int i = 0; i < tray.length; i++)
            tray[i] = BoardService.getRandomChar();

        User user = new User("RJamesRJ", "password1", "salt", "robertjames@gmail.com", 1000, 0, 0, false);
        Board board = new Board(UUID.randomUUID(), user, tray, 0, worms, letters, UUID.randomUUID(), true);


        long total = 0;


//        for (int i = 0; i < 10; i++){
//            board = new AIService(board).setWorms();
//
//            int count = 0;
//            for (int j = 0; j < 16; j++) {
//                for (int k = 0; k < 16; k++) {
//                    System.out.print(worms[count] + ", ");
//                    count++;
//                }
//                System.out.println();
//            }
//            System.out.println();
//
//            Arrays.fill(worms, '.');
//            board.setWorms(worms);
//
//            TimeUnit.SECONDS.sleep(2);
//        }

        for (int i = 0; i < 20; i++){
            long start = System.currentTimeMillis();

            board = new AIService(board).start(start, 1);

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
        System.out.println(total / 20);
        System.out.println(board.getFireballs());
    }
}