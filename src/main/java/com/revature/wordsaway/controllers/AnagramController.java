package com.revature.wordsaway.controllers;

import com.revature.wordsaway.dtos.requests.AnagramRequest;
import com.revature.wordsaway.services.AnagramService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class AnagramController {
    // TODO remove from final to prevent cheating or make only accessible by CUPs
    @CrossOrigin
    @GetMapping(value = "/best", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String testBest(@RequestBody AnagramRequest request){
        return AnagramService.getBest(request.getLetters());
    }

    // TODO remove from final to prevent cheating or make only accessible by CUPs
    @CrossOrigin
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody String testAll(@RequestBody AnagramRequest request){
        return AnagramService.getAll(request.getLetters());
    }

    @CrossOrigin
    @GetMapping(value = "/lookup", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody boolean testIsWord(@RequestBody AnagramRequest request){
        return AnagramService.isWord(request.getLetters());
    }
}
