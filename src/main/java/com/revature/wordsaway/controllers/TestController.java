package com.revature.wordsaway.controllers;

import com.revature.wordsaway.dtos.requests.LoginRequest;
import com.revature.wordsaway.dtos.requests.NewUserRequest;
import com.revature.wordsaway.models.User;
import com.revature.wordsaway.services.UserService;
import com.revature.wordsaway.utils.customExceptions.AuthenticationException;
import com.revature.wordsaway.utils.customExceptions.InvalidRequestException;
import com.revature.wordsaway.utils.customExceptions.ResourceConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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