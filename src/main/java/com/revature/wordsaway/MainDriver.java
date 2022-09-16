package com.revature.wordsaway;

import com.revature.wordsaway.models.Board;
import com.revature.wordsaway.models.User;
import com.revature.wordsaway.services.AIService;
import com.revature.wordsaway.services.AnagramService;

import java.util.*;

//@SpringBootApplication
public class MainDriver {
    static char[] tray = "uitsare".toUpperCase().toCharArray();
    static char[] letters = new char[16*16];

    public static void main(String[] args) {
        //SpringApplication.run(MainDriver.class, args);

        //    0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5
     // 00 0, ., ., ., ., ., ., ., ., ., ., ., ., M, ., ., .,
     // 16 1, ., ., ., ., ., ., ., ., ., ., ., ., E, ., ., .,
     // 32 2, ., ., ., ., ., ., ., ., ., ., ., ., L, ., ., .,
     // 48 3, ., ., L, ., L, ., ., ., ., S, ., ., T, ., ., .,
     // 64 4, ., ., U, ., I, ., ., D, ., E, ., ., ., ., ., .,
     // 80 5, ., ., M, ., F, ., ., R, ., A, ., ., ., ., ., .,
     // 96 6, ., ., B, ., T, ., ., E, ., S, ., ., ., ., ., .,
     // 12 7, ., ., E, ., ., ., ., A, ., O, ., ., ., ., ., .,
     // 28 8, ., ., R, ., ., ., ., M, ., N, ., ., ., ., ., .,
     // 44 9, ., ., ., ., ., ., ., ., ., ., ., ., ., ., ., .,
     // 60 0, ., ., ., ., ., ., ., ., ., ., ., ., ., ., ., .,
     // 76 1, ., ., ., ., R, O, U, T, E, ., ., ., ., ., ., .,
     // 92 2, ., ., ., ., ., ., ., ., ., ., ., ., ., ., ., .,
     // 08 3, ., ., ., ., ., ., ., ., ., ., ., ., ., ., ., .,
     // 24 4, ., ., ., ., ., ., ., ., ., ., ., ., ., ., ., .,
     // 40 5, ., ., ., ., ., ., ., ., ., ., ., ., ., ., ., .,

        Arrays.fill(letters, '.');
        letters[50] = 'L';
        letters[66] = 'U';
        letters[82] = 'M';
        letters[98] = 'B';
        letters[114] = 'E';
        letters[130] = 'R';

        letters[52] = 'L';
        letters[68] = 'I';
        letters[84] = 'F';
        letters[100] = 'T';

        letters[71] = 'D';
        letters[87] = 'R';
        letters[103] = 'E';
        letters[119] = 'A';
        letters[135] = 'M';

        letters[57] = 'S';
        letters[73] = 'E';
        letters[89] = 'A';
        letters[105] = 'S';
        letters[121] = 'O';
        letters[137] = 'N';

        letters[180] = 'R';
        letters[181] = 'O';
        letters[182] = 'U';
        letters[183] = 'T';
        letters[184] = 'E';

        letters[12] = 'M';
        letters[28] = 'E';
        letters[44] = 'L';
        letters[60] = 'T';

        User user = new User("RJamesRJ", "password1", "salt", "robertjames@gmail.com", 1000, 0, 0, false);
        Board board = new Board(UUID.randomUUID(), user, tray, 0, new char[2], letters, UUID.randomUUID(), true);
        List<String> list = new ArrayList<>();
        long total = 0;

        for (int i = 0; i < 10; i++){
            long start = System.currentTimeMillis();

            board = new AIService(board).startEasyBot(start);

            list.add(String.valueOf(board.getTray()));

            total += (System.currentTimeMillis() - start) / 1000;
        }

        letters = board.getLetters();
        int count = 0;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                System.out.print(letters[count] + ", ");
                count++;
            }
            System.out.println();
        }
        System.out.println(list);
        System.out.println(total / 10);
        System.out.println(board.getFireballs());
    }
}