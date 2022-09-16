package com.revature.wordsaway.controllers;

import com.revature.wordsaway.dtos.requests.AnagramRequest;
import com.revature.wordsaway.services.AnagramService;
import com.revature.wordsaway.utils.customExceptions.NetworkException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AnagramController {
    //TODO remove from final to prevent cheating or make only accessible by CUPs
    @CrossOrigin
    @GetMapping(value = "/best", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String testBest(@RequestBody AnagramRequest request){
        try {
            return AnagramService.getBest(request.getLetters());
        }catch (NetworkException e){
            return e.getMessage();
        }
    }

    //TODO remove from final to prevent cheating or make only accessible by CUPs
    @CrossOrigin
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String testAll(@RequestBody AnagramRequest request){
        try{
            return AnagramService.getAll(request.getLetters());
        } catch (NetworkException e){
            return e.getMessage();
        }
    }

    @CrossOrigin
    @GetMapping(value = "/lookup", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody boolean testIsWord(@RequestBody AnagramRequest request){
        try {
            return AnagramService.isWord(request.getLetters());
        }catch (NetworkException e){
            return false;
        }
    }
}
