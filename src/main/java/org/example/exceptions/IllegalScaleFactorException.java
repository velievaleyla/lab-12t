package org.example.exceptions;

public class IllegalScaleFactorException extends RuntimeException {
    public IllegalScaleFactorException() {
        super("Illegal scale factor. Factor must be greater than 0.");
    }

    public IllegalScaleFactorException(String message) {
        super(message);
    }
}
