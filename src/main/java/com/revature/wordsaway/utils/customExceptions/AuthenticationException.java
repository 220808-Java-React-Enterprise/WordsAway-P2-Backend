package com.revature.wordsaway.utils.CustomExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AuthenticationException extends NetworkException{
    public AuthenticationException(String message) {
        super(message);
    }
}
