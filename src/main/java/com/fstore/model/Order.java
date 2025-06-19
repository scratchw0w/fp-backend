package com.fstore.model;

import lombok.With;

import java.time.LocalDateTime;

@With
public record Order(
    int id,
    String name,
    String email,
    String phoneNumber,
    String address,
    String comment,
    LocalDateTime deliveryTime,
    OrderStatus status,
    LocalDateTime timestamp,
    int productId) {
}
