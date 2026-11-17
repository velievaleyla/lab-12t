package org.example.exceptions;

public class IllegalInputTimeSpanException extends RuntimeException {
    public IllegalInputTimeSpanException() {
        super("Illegal input time span arguments.");
    }

    public IllegalInputTimeSpanException(String message) {
        super(message);
    }
}
