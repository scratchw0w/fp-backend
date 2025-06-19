package com.fstore.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateOrderRequestDto(
        @NotEmpty(message = "Name cannot be empty")
        String name,
        @NotEmpty(message = "Email cannot be empty")
        @Email(message = "Email should be valid")
        String email,
        @NotEmpty(message = "Phone number cannot be empty")
        @JsonProperty("phone_number")
        String phoneNumber,
        @NotEmpty(message = "Address cannot be empty")
        String address,
        String comment,
        @NotNull(message = "Delivery time cannot be null")
        @JsonProperty("delivery_time")
        LocalDateTime deliveryTime,
        @NotNull(message = "Product id cannot be null")
        @JsonProperty("product_id")
        Integer productId) {
}
