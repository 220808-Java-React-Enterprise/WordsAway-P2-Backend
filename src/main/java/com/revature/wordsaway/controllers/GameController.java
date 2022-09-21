package com.revature.wordsaway.controllers;

import com.revature.wordsaway.dtos.requests.BoardRequest;
import com.revature.wordsaway.dtos.requests.GameRequest;
import com.revature.wordsaway.dtos.responses.OpponentResponse;
import com.revature.wordsaway.models.Board;
import com.revature.wordsaway.models.User;
import com.revature.wordsaway.services.AIService;
import com.revature.wordsaway.services.BoardService;
import com.revature.wordsaway.services.TokenService;
import com.revature.wordsaway.services.UserService;
import com.revature.wordsaway.utils.customExceptions.ForbiddenException;
import com.revature.wordsaway.utils.customExceptions.InvalidRequestException;
import com.revature.wordsaway.utils.customExceptions.NetworkException;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import static com.revature.wordsaway.utils.Constants.BOARD_SIZE;

@RestController
@RequestMapping
public class GameController {
    @CrossOrigin
    @PostMapping(value = "/makeGame", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody String makeGame(@RequestBody GameRequest request, HttpServletResponse resp, HttpServletRequest req) {
        try {
            //TODO check for existing game with opponent
            UUID uuid = UUID.randomUUID();
            BoardService.register(UserService.getByUsername(request.getUsername()), uuid, true);
            BoardService.register(TokenService.extractRequesterDetails(req), uuid, false);
            return uuid.toString();
        }catch (NetworkException e){
            resp.setStatus(e.getStatusCode());
            return e.getMessage();
        }
    }

    @CrossOrigin
    @GetMapping(value = "/getGame", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getGame(@Param("id") String id, HttpServletResponse resp) {
        try {

            return BoardService.getByGameID(UUID.fromString(id)).toString();
        }catch (NetworkException e){
            resp.setStatus(e.getStatusCode());
            return e.getMessage();
        }
    }

    @CrossOrigin
    @PostMapping(value = "/placeWorms", consumes = "application/json")
    public String placeWorms(@RequestBody BoardRequest request, HttpServletRequest httpServletRequest, HttpServletResponse resp) {
        try {
            User user = TokenService.extractRequesterDetails(httpServletRequest);
            Board board = BoardService.getByID(request.getBoardID());
            Board opposingBoard = BoardService.getOpposingBoard(board);
            User opponent = opposingBoard.getUser();
            if (opponent.isCPU()) new AIService(opposingBoard).setWorms();
            else board.setWorms(request.getLayout());
            BoardService.update(board);
            return "Worms placed.";
        }catch (NetworkException e){
            resp.setStatus(e.getStatusCode());
            return e.getMessage();
        }
    }

    @CrossOrigin
    @GetMapping(value = "/checkMove", consumes = "application/json")
    public boolean checkMove(@RequestBody BoardRequest request, HttpServletResponse resp) {
        //TODO possibly change to use params
        try {
            BoardService.validateMove(request);
            return true;
        }catch (NetworkException e){
            resp.setStatus(e.getStatusCode());
            System.out.println(e.getMessage());
            return false;
        }
    }

    @CrossOrigin
    @PostMapping(value = "/makeMove", consumes = "application/json")
    public String makeMove(@RequestBody BoardRequest request, HttpServletRequest httpServletRequest, HttpServletResponse resp) {
        try {
            User user = TokenService.extractRequesterDetails(httpServletRequest);
            Board board = BoardService.getByID(request.getBoardID());
            if(!board.getUser().equals(user)) throw new ForbiddenException("Can not make move on board you don't own.");
            if(!board.isActive()) throw new ForbiddenException("Can not make move on board when it is not your turn.");
            BoardService.makeMove(request, board);
            //TODO maybe tell opponent that it's their turn if not checking continuously
            return "Move made.";
        }catch (NetworkException e){
            resp.setStatus(e.getStatusCode());
            return e.getMessage();
        }
    }

    @CrossOrigin
    @GetMapping(value = "/getChecked", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getChecked(@Param("id") String id, HttpServletResponse resp) {
        try {
            return Arrays.toString(BoardService.getChecked(BoardService.getByID(UUID.fromString(id)).getLetters()));
        }catch (NetworkException e){
            resp.setStatus(e.getStatusCode());
            return e.getMessage();
        }
    }

    @CrossOrigin
    @GetMapping(value = "/getHits", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String getHits(@Param("id") String id, HttpServletResponse resp) {
        try {
            boolean[] hits = new boolean[BOARD_SIZE*BOARD_SIZE];
            Board board = BoardService.getByID(UUID.fromString(id));
            char[] worms = BoardService.getOpposingBoard(board).getWorms();
            boolean[] checked = BoardService.getChecked(board.getLetters());
            for (int i = 0; i < hits.length; i++) {
                hits[i] = worms[i] != '.' && checked[i];
            }
            return Arrays.toString(hits);
        }catch (NetworkException e){
            resp.setStatus(e.getStatusCode());
            return e.getMessage();
        }
    }

    @CrossOrigin
    @GetMapping(value = "/getOpponents", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<OpponentResponse> getOpponents(HttpServletRequest httpServletRequest, HttpServletResponse resp) {
        try {
            return UserService.getAllOpponents(TokenService.extractRequesterDetails(httpServletRequest).getUsername());
        }catch(NetworkException e){
            resp.setStatus(e.getStatusCode());
            System.out.println(e.getMessage());
            return null;
        }
    }
}
