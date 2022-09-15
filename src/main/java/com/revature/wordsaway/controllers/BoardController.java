package com.revature.wordsaway.controllers;

import com.revature.wordsaway.dtos.requests.MoveRequest;
import com.revature.wordsaway.models.Board;
import com.revature.wordsaway.models.User;
import com.revature.wordsaway.services.BoardService;
import com.revature.wordsaway.services.TokenService;
import com.revature.wordsaway.services.UserService;
import com.revature.wordsaway.utils.customExceptions.AuthenticationException;
import com.revature.wordsaway.utils.customExceptions.ForbiddenException;
import com.revature.wordsaway.utils.customExceptions.InvalidRequestException;
import com.revature.wordsaway.utils.customExceptions.NetworkException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping
public class BoardController {

    @CrossOrigin
    @GetMapping(value = "/checkMove", consumes = "application/json")
    public boolean checkMove(@RequestBody MoveRequest request) {
        try {
            BoardService.validateMove(request);
        }catch (InvalidRequestException e){
            return false;
        }
        return true;
    }

    @CrossOrigin
    @PostMapping(value = "/makeMove", consumes = "application/json")
    public String makeMove(@RequestBody MoveRequest request, HttpServletRequest httpServletRequest) {
        try {
            User user = TokenService.extractRequesterDetails(httpServletRequest);
            Board board = BoardService.getByID(request.getBoardID());
            if(!board.getUser().equals(user)) throw new ForbiddenException("Can not make move on board you don't own.");
            if(!board.isActive()) throw new ForbiddenException("Can not make move on board when it is not your turn.");
            BoardService.validateMove(request);
            Board opposingBoard = BoardService.getOpposingBoard(board);
            board.setLetters(request.getMove());
            board.toggleActive();
            opposingBoard.toggleActive();
            BoardService.update(board);
            BoardService.update(opposingBoard);
        }catch (NetworkException e){
            return e.getMessage();
        }
        return "Move made.";
    }
}
