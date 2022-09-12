package com.revature.wordsaway.controllers;

import com.revature.wordsaway.models.User;
import com.revature.wordsaway.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping
public class TestController {
    //TODO delete this class at the end

    @GetMapping(value = "/getall", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<User> getAll() {
        return UserService.getAll();
    }
}