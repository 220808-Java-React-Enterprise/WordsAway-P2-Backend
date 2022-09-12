package com.revature.wordsaway.utils.CustomExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends NetworkException{
    public NotFoundException(String message) {
        super(message);
    }
}
