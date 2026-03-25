package org.example.exceptions;

public class NoMoreNumbersAvailableException extends RandomGeneratorException {
    public NoMoreNumbersAvailableException(String message) {
        super(message);
    }
    public NoMoreNumbersAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
    public NoMoreNumbersAvailableException(Throwable cause) {
        super(cause);
    }
    public NoMoreNumbersAvailableException() {
        super();
    }

}
