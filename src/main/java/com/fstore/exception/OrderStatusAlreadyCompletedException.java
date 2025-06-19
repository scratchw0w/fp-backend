package com.fstore.exception;

public class OrderStatusAlreadyCompletedException extends RuntimeException {
    public OrderStatusAlreadyCompletedException(String message) {
        super(message);
    }
}
