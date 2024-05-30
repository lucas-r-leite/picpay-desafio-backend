package com.example.desafio_backend.error;

public class InsufficientFundsException  extends RuntimeException { 

    public InsufficientFundsException(String message) {
        super(message);
    }

}
