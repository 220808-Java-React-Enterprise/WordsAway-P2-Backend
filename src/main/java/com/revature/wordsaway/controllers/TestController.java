package com.revature.wordsaway.controllers;

import com.revature.wordsaway.models.Board;
import com.revature.wordsaway.models.User;
import com.revature.wordsaway.services.BoardService;
import com.revature.wordsaway.services.UserService;
import com.revature.wordsaway.utils.customExceptions.NetworkException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/test")
public class TestController {
    //TODO delete this class at the end

    @CrossOrigin
    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<User> getAll() {
        return UserService.getAll();
    }

    @CrossOrigin
    @PostMapping(value = "/makeGame", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody String makeGame(HttpServletResponse resp) {
        try {
            List<Board> boards = new ArrayList<>();
            UUID uuid = UUID.randomUUID();
            boards.add(BoardService.register(UserService.getByUsername("RJamesRJ"), uuid, true));
            boards.add(BoardService.register(UserService.getByUsername("Easy-Bot"), uuid, false));
            return boards.toString();
        }catch (NetworkException e){
            resp.setStatus(e.getStatusCode());
            return e.getMessage();
        }
    }
}