package org.example.exceptions;

public class AccountHasBalanceException extends AccountException {
    public AccountHasBalanceException(String message) {
        super(message);
    }
}
