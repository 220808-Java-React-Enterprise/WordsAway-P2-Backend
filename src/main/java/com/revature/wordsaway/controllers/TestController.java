package com.revature.wordsaway.controllers;

import com.revature.wordsaway.dtos.requests.AnagramRequest;
import com.revature.wordsaway.models.Board;
import com.revature.wordsaway.models.Game;
import com.revature.wordsaway.models.User;
import com.revature.wordsaway.services.BoardService;
import com.revature.wordsaway.services.UserService;
import com.revature.wordsaway.utils.customExceptions.InvalidRequestException;
import com.revature.wordsaway.utils.customExceptions.ResourceConflictException;
import com.sun.javaws.exceptions.InvalidArgumentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.revature.wordsaway.utils.Constants.BOARD_SIZE;

@RestController
@RequestMapping
public class TestController {
    //TODO delete this class at the end

    @GetMapping(value = "/getall", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<User> getAll() {
        return UserService.getAll();
    }

    @PostMapping(value = "/makegame", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody String makeGame() {
        return new Game(UserService.getByUsername("koukaakiva"), UserService.getByUsername("christhewizard")).toString();
    }

    @ExceptionHandler(value = {InvalidArgumentException.class})
    @GetMapping(value = "/getgame", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody char[] getGame(@RequestBody AnagramRequest gameID) {
        Game game = new Game(UUID.fromString(gameID.getLetters()));
        return game.getBoard(0).getColumn(0);
    }

    @PostMapping(value = "/deleteAllGames")
    public void deleteAllGames(){
        BoardService.deleteAll();
    }

    @ExceptionHandler(value = {InvalidRequestException.class})
    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody void test() {
        char[] move = new char[BOARD_SIZE*BOARD_SIZE];
        Arrays.fill(move, '.');
        move[1] = 'P';
        move[2] = 'L';
        move[3] = 'O';
        move[4] = 'P';
        move[48] = 'S';
        BoardService.validateMove(UUID.fromString("4c3794b2-750e-4b68-a462-0c751e684566"), move);
    }
}