package org.example.exceptions;

public class NoEmptyConstructorException extends RuntimeException {
    public NoEmptyConstructorException(String message) {
        super(message);
    }
    public NoEmptyConstructorException(String message, Throwable cause) {
        super(message, cause);
    }
    public NoEmptyConstructorException(Throwable cause) {
        super(cause);
    }
    public NoEmptyConstructorException() {
        super();
    }
}
