package com.example.xpresspaymentapi.configuration.exception;

public class AirtimePurchaseException extends RuntimeException{
    public AirtimePurchaseException() {
    }

    public AirtimePurchaseException(String message) {
        super(message);
    }

    public AirtimePurchaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
