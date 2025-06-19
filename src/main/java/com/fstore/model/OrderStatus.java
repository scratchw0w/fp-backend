package com.fstore.model;

import com.fstore.exception.OrderStatusAlreadyCompletedException;

public enum OrderStatus {
    CREATED {
        @Override
        public OrderStatus goNext() {
            return IN_PROGRESS;
        }
    },
    IN_PROGRESS {
        @Override
        public OrderStatus goNext() {
            return COMPLETED;
        }

    },
    COMPLETED {
        @Override
        public OrderStatus goNext() {
            throw new OrderStatusAlreadyCompletedException("The order is already completed");
        }
    };

    public OrderStatus goNext() {
        throw new UnsupportedOperationException();
    }
}
