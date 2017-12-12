package com.example.jlupin.session.exception;

/**
 * @author Piotr Heilman
 */
public class CoreSystemUnavailableException extends RuntimeException{
    public CoreSystemUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
