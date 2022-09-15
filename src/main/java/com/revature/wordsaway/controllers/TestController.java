package com.revature.wordsaway.controllers;

import com.revature.wordsaway.dtos.requests.AnagramRequest;
import com.revature.wordsaway.dtos.requests.MoveRequest;
import com.revature.wordsaway.models.Game;
import com.revature.wordsaway.models.User;
import com.revature.wordsaway.services.BoardService;
import com.revature.wordsaway.services.UserService;
import com.revature.wordsaway.utils.customExceptions.NetworkException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
public class TestController {
    //TODO delete this class at the end

    @CrossOrigin
    @GetMapping(value = "/getall", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<User> getAll() {
        return UserService.getAll();
    }

    @CrossOrigin
    @PostMapping(value = "/makegame", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody String makeGame() {
        return new Game(UserService.getByUsername("koukaakiva"), UserService.getByUsername("christhewizard")).toString();
    }

    @CrossOrigin
    @GetMapping(value = "/getgame", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getGame(@RequestBody AnagramRequest gameID) {
        Game game = new Game(UUID.fromString(gameID.getLetters()));
        return game.toString();
    }

    @CrossOrigin
    @PostMapping(value = "/deleteAllGames")
    public void deleteAllGames(){
        BoardService.deleteAll();
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public @ResponseBody NetworkException handleException(NetworkException e) {
        return e;
    }
}