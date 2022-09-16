package com.revature.wordsaway.controllers;

import com.revature.wordsaway.dtos.requests.LoginRequest;
import com.revature.wordsaway.dtos.requests.NewUserRequest;
import com.revature.wordsaway.dtos.requests.UsernameRequest;
import com.revature.wordsaway.models.User;
import com.revature.wordsaway.services.UserService;
import com.revature.wordsaway.utils.customExceptions.AuthenticationException;
import com.revature.wordsaway.utils.customExceptions.InvalidRequestException;
import com.revature.wordsaway.utils.customExceptions.NetworkException;
import com.revature.wordsaway.utils.customExceptions.ResourceConflictException;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping
public class AccessController {

    @CrossOrigin
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(value = "/signup", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String signup(@RequestBody NewUserRequest request, HttpServletResponse resp) {
        try {
            return UserService.register(request).toString();
        }catch (NetworkException e){
            resp.setStatus(e.getStatusCode());
            return e.getMessage();
        }
    }

    @CrossOrigin
    @PostMapping(value = "/login", consumes = "application/json")
    public String login(@RequestBody LoginRequest request, HttpServletResponse resp) {
        try {
            String token = UserService.login(request);
            resp.setHeader("Authorization", token);
            resp.setHeader("Access-Control-Expose-Headers", "Authorization");
            return "Logged In";
        }catch (NetworkException e){
            resp.setStatus(e.getStatusCode());
            return e.getMessage();
        }
    }

    @CrossOrigin
    @GetMapping(value = "/salt")
    public String salt(@Param("username") String username, HttpServletResponse resp) {
        User user;
        try{
            user = UserService.getByUsername(username);
        }catch (NetworkException e){
            resp.setStatus(e.getStatusCode());
            return UUID.randomUUID().toString().replace("-","");
        }
        return user.getSalt();
    }
}