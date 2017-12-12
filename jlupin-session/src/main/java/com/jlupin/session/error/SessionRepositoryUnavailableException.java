package com.jlupin.session.error;

/**
 * @author Piotr Heilman
 */
public class SessionRepositoryUnavailableException extends RuntimeException {
    public SessionRepositoryUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
