package com.fstore.exception;

public class ProductNotRemovedException extends RuntimeException {
    public ProductNotRemovedException(String message) {
        super(message);
    }
}
