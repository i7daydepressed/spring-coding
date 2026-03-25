package org.example.exceptions;

public class RandomGeneratorException extends Exception {
    public RandomGeneratorException(String message) {
        super(message);
    }
    public RandomGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }
    public RandomGeneratorException(Throwable cause) {
        super(cause);
    }
    public RandomGeneratorException() {
        super();
    }
    
}
