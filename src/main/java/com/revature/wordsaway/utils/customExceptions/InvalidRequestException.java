package com.revature.wordsaway.utils.CustomExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends NetworkException{
    public InvalidRequestException(String message) {
        super(message);
    }
}
