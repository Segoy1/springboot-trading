package de.segoy.springboottradingibkr.client.exception;

public class TWSConnectionException extends RuntimeException {
    public TWSConnectionException(String message) {
        super(message);
    }
    public TWSConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
