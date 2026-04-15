package org.jdbcc.exceptions;

public class NotFoundPKException extends RuntimeException {
    public NotFoundPKException(String message) {
        super(message);
    }
    public NotFoundPKException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundPKException(Throwable cause) {
        super(cause);
    }
    public NotFoundPKException() {
        super();
    }
}
