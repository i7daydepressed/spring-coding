package org.jdbcc.exceptions;

public class GetValueException extends RuntimeException {
    public GetValueException(String message) {
        super(message);
    }
    public GetValueException(String message, Throwable cause) {
        super(message, cause);
    }
    public GetValueException(Throwable cause) {
        super(cause);
    }
    public GetValueException() {
        super();
    }

}
