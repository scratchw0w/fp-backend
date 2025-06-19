package com.fstore.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fstore.model.OrderStatus;

import java.time.LocalDateTime;

public record OrderResponseDto(
        int id,
        String name,
        String email,
        @JsonProperty("phone_number")
        String phoneNumber,
        String address,
        String comment,
        @JsonProperty("delivery_time")
        LocalDateTime deliveryTime,
        OrderStatus status,
        LocalDateTime timestamp,
        @JsonProperty("product_id")
        int productId) {
}
