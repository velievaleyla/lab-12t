package org.example.exceptions;

public class NegativeTimeSpanException extends RuntimeException {
    public NegativeTimeSpanException() {
        super("TimeSpan value cannot be negative.");
    }

    public NegativeTimeSpanException(String message) {
        super(message);
    }
}
