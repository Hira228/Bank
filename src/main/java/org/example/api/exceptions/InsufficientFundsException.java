package org.example.api.exceptions;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(String err) {
        super(err);
    }
}
