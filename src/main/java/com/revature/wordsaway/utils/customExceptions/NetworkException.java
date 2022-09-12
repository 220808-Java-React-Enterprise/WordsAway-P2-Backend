package com.revature.wordsaway.utils.CustomExceptions;

public abstract class NetworkException extends RuntimeException {
    public NetworkException(String message) {
        super(message);
    }
}
