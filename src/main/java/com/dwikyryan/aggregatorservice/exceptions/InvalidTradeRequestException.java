package com.dwikyryan.aggregatorservice.exceptions;

public class InvalidTradeRequestException extends RuntimeException {
    public InvalidTradeRequestException(String message) {
        super(message);
    }
}
