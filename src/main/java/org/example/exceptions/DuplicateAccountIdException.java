package org.example.exceptions;

public class DuplicateAccountIdException extends AccountException {
    public DuplicateAccountIdException(String message) {
        super(message);
    }
}
