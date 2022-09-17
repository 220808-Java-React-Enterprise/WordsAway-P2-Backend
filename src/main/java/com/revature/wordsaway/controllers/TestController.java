package com.revature.wordsaway.controllers;

import com.revature.wordsaway.models.User;
import com.revature.wordsaway.services.BoardService;
import com.revature.wordsaway.services.GameService;
import com.revature.wordsaway.services.TokenService;
import com.revature.wordsaway.services.UserService;
import com.revature.wordsaway.utils.customExceptions.NetworkException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
    public @ResponseBody String makeGame() {
        return GameService.register(UserService.getByUsername("koukaakiva"), UserService.getByUsername("christhewizard")).toString();
    }

    @CrossOrigin
    @GetMapping(value = "/auth")
    public String makeMove(HttpServletRequest httpServletRequest, HttpServletResponse resp) {
        try {
            return TokenService.extractRequesterDetails(httpServletRequest).toString();
        } catch (NetworkException e) {
            resp.setStatus(e.getStatusCode());
            return e.getMessage();
        }
    }
}