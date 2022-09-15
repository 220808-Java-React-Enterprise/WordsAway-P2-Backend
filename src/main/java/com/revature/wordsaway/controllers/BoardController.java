package com.revature.wordsaway.controllers;

import com.revature.wordsaway.dtos.requests.MoveRequest;
import com.revature.wordsaway.services.BoardService;
import com.revature.wordsaway.utils.customExceptions.InvalidRequestException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class BoardController {

    @CrossOrigin
    @GetMapping(value = "/checkMove", consumes = "application/json")
    public boolean checkMove(@RequestBody MoveRequest request) {
        try {
            BoardService.validateMove(request.getBoardID(), request.getMove());
        }catch (InvalidRequestException e){
            return false;
        }
        return true;
    }
}
