package com.fstore.exception;

public class OrderNotUpdatedException extends RuntimeException {
    public OrderNotUpdatedException(String message) {
        super(message);
    }
}
