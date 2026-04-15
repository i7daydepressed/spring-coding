package org.jdbcc.exceptions;

public class GetPKException extends GetValueException {
    public GetPKException(String message) {
        super(message);
    }
    public GetPKException(String message, Throwable cause) {
        super(message, cause);
    }
    public GetPKException(Throwable cause) {
        super(cause);
    }
    public GetPKException() {
        super();
    }
}
